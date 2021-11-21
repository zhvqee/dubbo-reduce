package org.qee.cloud.spring.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.qee.cloud.common.exceptions.InjectException;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.rpc.annotation.CloudReference;
import org.qee.cloud.spring.config.CloudReferenceConfigBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CloudReferenceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {


    private static ConcurrentHashMap<String, AnnotatedInjectionMetadata> cache = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Object> injectedObjectMap = new ConcurrentHashMap<>();

    private Class<? extends Annotation> doWithAnnotation = CloudReference.class;

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    /**
     * 该节点在Bean 的 注入属性时机，在初始化方法之前
     *
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        InjectionMetadata metadata = findInjectionMetadata(beanName, bean);
        try {
            if (metadata != null) {
                metadata.inject(bean, beanName, pvs);
            }
        } catch (Throwable throwable) {
            Throws.throwException(InjectException.class, throwable.getMessage());
        }
        return pvs;
    }

    /**
     * 查找该注解的需要注入的信息
     *
     * @param bean
     * @return
     */
    private InjectionMetadata findInjectionMetadata(String beanName, Object bean) {
        String key = Optional.ofNullable(beanName).orElse(bean.getClass().getName());
        AnnotatedInjectionMetadata annotatedInjectionMetadata = cache.get(key);
        if (annotatedInjectionMetadata == null) {
            synchronized (cache) {
                if (annotatedInjectionMetadata == null) {
                    annotatedInjectionMetadata = buildAnnotatedInjectionMetadata(bean.getClass());
                    if (annotatedInjectionMetadata == null) {
                        return null;
                    }
                    cache.put(key, annotatedInjectionMetadata);
                }
            }
        }
        return annotatedInjectionMetadata;
    }

    /**
     * @param beanClass 宿主 bean
     * @return
     */
    private AnnotatedInjectionMetadata buildAnnotatedInjectionMetadata(Class<?> beanClass) {

        List<InjectionMetadata.InjectedElement> injectionMetadataList = new ArrayList<>();
        ReflectionUtils.doWithFields(beanClass, field -> {
            if (field.getAnnotation(CloudReference.class) == null) {
                return;
            }
            AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(field, field.getAnnotation(CloudReference.class), false, true);
            injectionMetadataList.add(new AnnotatedInjectionElement(field, annotationAttributes));
        });
        if (CollectionUtils.isEmpty(injectionMetadataList)) {
            return null;
        }
        return new AnnotatedInjectionMetadata(beanClass, injectionMetadataList);
    }


    static class AnnotatedInjectionMetadata extends InjectionMetadata {

        public AnnotatedInjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
            super(targetClass, elements);
        }
    }

    class AnnotatedInjectionElement extends InjectionMetadata.InjectedElement {

        private final Field field;

        private final AnnotationAttributes annotationAttributes;

        protected AnnotatedInjectionElement(Field field, AnnotationAttributes annotationAttributes) {
            super(field, null);
            this.field = field;
            this.annotationAttributes = annotationAttributes;
        }

        @Override
        protected void inject(Object target, String requestingBeanName, PropertyValues pvs) throws Throwable {
            Object value = getInjectedObject(target, requestingBeanName, annotationAttributes, this.field.getType());
            this.field.setAccessible(true);
            this.field.set(target, value);
        }
    }

    private Object getInjectedObject(Object target, String requestingBeanName, AnnotationAttributes annotationAttributes, Class<?> injectClassType) {


        //1、得到 一个 注入 的beanName
        String injectedObjectBeanName = getInjectedObjectBeanName(annotationAttributes, injectClassType);
        Object injectedObject = null;
        if ((injectedObject = injectedObjectMap.get(injectedObjectBeanName)) == null) {
            synchronized (injectedObjectMap) {
                if (injectedObjectMap.get(injectedObjectBeanName) == null) {
                    //2、构建CloudReferenceConfig
                    CloudReferenceConfigBean<?> configBean = new CloudReferenceConfigBean<>();
                    String id = (String) annotationAttributes.get("id");
                    configBean.setId(Optional.ofNullable(id).orElse(injectedObjectBeanName));
                    configBean.setInterfaceName(injectClassType.getName());
                    configBean.setInterfaceClass((Class) injectClassType);
                    configBean.setGroup((String) annotationAttributes.get("group"));
                    configBean.setVersion((String) annotationAttributes.get("version"));
                    configBean.setCheck((Boolean) annotationAttributes.get("check"));
                    configBean.setMock((String) annotationAttributes.get("mock"));
                    // 这里和进行服务引用入口

                    injectedObjectMap.put(injectedObjectBeanName, configBean.get());
                    beanFactory.registerSingleton(configBean.getId(), configBean);// config 是 FactoryBean ，所以注册是远程类型
                    injectedObject = injectedObjectMap.get(injectedObjectBeanName);
                }
            }
        }
        //
        return injectedObject;
    }

    private String getInjectedObjectBeanName(AnnotationAttributes annotationAttributes, Class<?> injectClassType) {
        return RemoteObjectUtils.getRemoteObjectInSpring(annotationAttributes, injectClassType);
    }

}
