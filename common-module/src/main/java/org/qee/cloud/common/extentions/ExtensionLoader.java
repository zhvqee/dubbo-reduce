package org.qee.cloud.common.extentions;

import lombok.Data;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;
import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.AutoLoad;
import org.qee.cloud.common.annotations.BeanName;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.exceptions.SpiExtensionException;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.Asserts;
import org.qee.cloud.common.utils.GeneratorKeys;
import org.qee.cloud.common.utils.Reflects;
import org.qee.cloud.common.utils.Throws;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

    private Class<T> refClass;


    private static ConcurrentHashMap<Class<?>, ExtensionLoader<?>> LOADER_MAP = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Holder<Object>> NAME_OBJ_MAP = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Class<?>, Object> CLASS_TO_INS_MAP = new ConcurrentHashMap<>();

    private static ObjectFactory objectFactory = null;

    private ConcurrentHashMap<String, Class<?>> NAME_CLASS_MAP = new ConcurrentHashMap<>();

    private String defaultName;

    private Holder<T> adaptiveExtensionHolder = new Holder<>();

    private static String ADAPTIVE_NAME = "cloud-adaptive";


    private volatile boolean inited;
//    static AtomicIntegerFieldUpdater CLOSING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DruidPooledConnection.class, "closing");


    static {
        objectFactory = ExtensionLoader.getExtensionLoader(ObjectFactory.class).getAdaptiveExtension();

    }

    private ExtensionLoader(Class<T> clzz) {
        this.refClass = clzz;
        defaultName = clzz.getAnnotation(SPI.class).name();
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        Asserts.assertTrue(type.isInterface(), SpiExtensionException.class, type.getName() + "不是一个接口");
        Asserts.assertTrue(type.getAnnotation(SPI.class), SpiExtensionException.class, type.getName() + "不是SPI 接口");
        ExtensionLoader<T> tExtensionLoader = (ExtensionLoader<T>) LOADER_MAP.computeIfAbsent(type, ExtensionLoader::new);
        return tExtensionLoader;
    }

    private T getExtension(String name, boolean isAdaptive) {
        Holder<Object> holder = NAME_OBJ_MAP.computeIfAbsent(name, k -> new Holder<>());
        if (holder.getValue() == null) {
            synchronized (holder) {
                if (holder.getValue() == null) {
                    Class<?> targetClass = getExtensionClasses().get(name);
                    if (!isAdaptive) { //不是adaptive 类必须存在
                        Asserts.assertTrue(targetClass != null, SpiExtensionException.class, "查找不到" + name + "扩展");
                    }
                    Object value = createOrGetExtensionInstance(targetClass);
                    holder.setValue(value);
                    try {
                        injectFiled(value, targetClass);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        Throws.throwException(SpiExtensionException.class, "class:" + targetClass.getName() + "设置参数错误");
                    }
                }
            }
        }
        return (T) holder.getValue();
    }

    public T getExtension(String name) {
        return getExtension(name, false);
    }


    /**
     * 创建extension实例， 这里不加锁，调用的地方已经加锁
     *
     * @param targetClass
     * @return
     */
    private Object createOrGetExtensionInstance(Class<?> targetClass) {
        if (targetClass == null) {
            return null;
        }
        Object instance = null;
        try {
            instance = targetClass.newInstance();
            CLASS_TO_INS_MAP.put(targetClass, instance);
            // injectFiled(instance, targetClass);
        } catch (InstantiationException | IllegalAccessException e) {
            Throws.throwException(SpiExtensionException.class, "class:" + targetClass.getName() + "没有默认构造函数");
        } /*catch (InvocationTargetException e) {
            Throws.throwException(SpiExtensionException.class, "class:" + targetClass.getName() + "设置参数错误");
        }*/
        // 这里该对象进行包裹，比如包裹一层 Mock对象

        return instance;
    }

    /**
     * 注入属性 setter方法注入,dubbo 是DisableInject ,不注入
     * 这里改为都要注入，但是如果指定BeanName,则用BeanName查找
     *
     * @param instance
     */
    private void injectFiled(Object instance, Class<?> targetClass) throws InvocationTargetException, IllegalAccessException {
        if (instance == null || targetClass == null) {
            return;
        }
        for (Method method : targetClass.getMethods()) {
            if (!Reflects.isSetter(method)) {
                continue;
            }
            if (Reflects.isPrimitives(method.getParameterTypes()[0])) {
                continue;
            }
            BeanName annotation = method.getAnnotation(BeanName.class);
            String beanName = null;
            if (annotation == null) {
                beanName = Reflects.getDefaultFieldName(method);
            } else {
                beanName = annotation.value();
            }
            Object object = objectFactory.getObject(beanName, method.getParameterTypes()[0]);
            if (object == null) {
                continue;
            }
            method.setAccessible(true);
            method.invoke(instance, object);
        }
    }

    /**
     * 同时获取是，只有一个线程能执行，其他线程阻塞，当需要获取资源是资源加载完成
     *
     * @return
     */
    private ConcurrentHashMap<String, Class<?>> getExtensionClasses() {
        if (inited) {
            return NAME_CLASS_MAP;
        }
        synchronized (NAME_CLASS_MAP) {
            if (!inited) {
                List<Properties> propertiesList = null;
                try {
                    propertiesList = ResourceLoader.loadExtension(Thread.currentThread().getContextClassLoader().getResources(refClass.getName()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Asserts.assertTrue(propertiesList, SpiExtensionException.class, "查找不到资源文件:" + refClass.getName());

                for (Properties properties : propertiesList) {
                    String textValue = (String) properties.get(Adaptive.class.getName());
                    resolvingConfig(textValue, false);

                    textValue = (String) properties.get(AutoLoad.class.getName());
                    resolvingConfig(textValue, true);
                }
                inited = true;
            }
        }
        return NAME_CLASS_MAP;
    }

    private void resolvingConfig(String textValue, boolean autoGenerateKey) {
        if (textValue != null && textValue.length() > 0) {
            String[] arrValues = StringUtils.split(textValue, ",");
            Arrays.stream(arrValues).forEach(value -> {
                String[] keyValue = value.split(":");
                if (autoGenerateKey) {
                    Asserts.assertTrue(keyValue.length == 1, SpiExtensionException.class, "资源文件配置错误:" + refClass.getName());
                } else {
                    Asserts.assertTrue(keyValue.length == 2, SpiExtensionException.class, "资源文件配置错误:" + refClass.getName());
                }
                Class<?> targetImplClass = null;
                String key = null;
                String classValue = null;
                try {
                    if (!autoGenerateKey) {
                        classValue = StringUtils.trim(keyValue[1]);
                        key = StringUtils.trim(keyValue[0]);
                    } else {
                        key = GeneratorKeys.generate("ext-load");
                        classValue = StringUtils.trim(keyValue[0]);
                    }
                    targetImplClass = Class.forName(classValue);
                    NAME_CLASS_MAP.put(key, targetImplClass);
                } catch (ClassNotFoundException e) {
                    Throws.throwException(SpiExtensionException.class, "class:" + keyValue[1] + " 查找不到");
                }
            });
        }
    }

    public List<String> getAllLoadExtensionName(boolean includeAdaptive) {
        getExtensionClasses();
        ArrayList<String> result = new ArrayList<>(NAME_CLASS_MAP.keySet());
        if (!includeAdaptive) {
            result.remove(ADAPTIVE_NAME);
        }
        return Collections.unmodifiableList(result);
    }

    public T getDefaultExtension() {
        return getExtension(defaultName);
    }


    //在配置中是否有一个default
    public T getAdaptiveExtension() {
        T adaptiveExtension = adaptiveExtensionHolder.getValue();
        if (adaptiveExtension == null) {
            synchronized (adaptiveExtensionHolder) {
                if (adaptiveExtension == null) {
                    adaptiveExtension = getExtension(ADAPTIVE_NAME, true);
                    adaptiveExtension = Optional.ofNullable(adaptiveExtension).orElseGet(this::createAdaptiveExtension);
                    adaptiveExtensionHolder.setValue(adaptiveExtension);
                }
            }
        }
        return adaptiveExtensionHolder.getValue();
    }

    @SneakyThrows
    private T createAdaptiveExtension() {
        ElementMatchers.annotationType(Adaptive.class);
        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
        Class<?> adaptiveClass = byteBuddy.subclass(Object.class)
                .name(refClass.getName() + "$Adaptive")
                .implement(refClass)
                .method(ElementMatchers.isAnnotatedWith(Adaptive.class))
                .intercept(InvocationHandlerAdapter.of(new AdaptiveInvocationHandler(refClass)))
                .make()
                .load(ExtensionLoader.class.getClassLoader())
                .getLoaded();
        Object newInstance = adaptiveClass.getConstructor(null).newInstance();

        Holder<Object> holder = new Holder<>();
        holder.setValue(newInstance);
        NAME_OBJ_MAP.put(ADAPTIVE_NAME, holder);

        if (!NAME_CLASS_MAP.contains(ADAPTIVE_NAME)) {
            NAME_CLASS_MAP.put(ADAPTIVE_NAME, newInstance.getClass());
        }

        return (T) newInstance;
    }


    static class AdaptiveInvocationHandler implements InvocationHandler {
        private Class<?> classRef;

        private String defaultName;

        public AdaptiveInvocationHandler(Class<?> classRef) {
            this.classRef = classRef;
            defaultName = classRef.getAnnotation(SPI.class).name();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args == null || args.length == 0) {
                Throws.throwException(SpiExtensionException.class, "class:" + method.getDeclaringClass() + "没有参数类型为" + URL.class.getName());
            }

            Object param = Arrays.stream(args).filter(arg -> URL.class.isAssignableFrom(arg.getClass())).findFirst().orElse(null);
            Asserts.assertTrue(param, SpiExtensionException.class, "class:" + method.getDeclaringClass() + "没有参数类型为" + URL.class.getName());

            Adaptive annotation = method.getAnnotation(Adaptive.class);
            String[] keys = annotation.keys();
            URL url = (URL) param;
            String name = Arrays.stream(keys).filter(key -> url.getParameter(key) != null).findFirst().orElse(defaultName);
            Object extension = ExtensionLoader.getExtensionLoader(classRef).getExtension(name);
            return method.invoke(extension, args);
        }
    }


    @Data
    static class Holder<T> {
        T value;

    }
}
