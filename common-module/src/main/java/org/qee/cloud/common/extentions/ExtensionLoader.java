package org.qee.cloud.common.extentions;

import lombok.Data;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.exceptions.SpiExtensionException;
import org.qee.cloud.common.utils.Asserts;
import org.qee.cloud.common.utils.Unsafe;

import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

    private Class<T> refClass;


    private static ConcurrentHashMap<Class<?>, ExtensionLoader<?>> LOADER_MAP = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Holder<?>> NAME_OBJ_MAP = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Class<?>> NAME_CLASS_MAP = new ConcurrentHashMap<>();

    private volatile boolean inited;


    static {
        Unsafe.getObjectOffset(this.inited)
    }

    private ExtensionLoader(Class<T> clzz) {
        Asserts.assertTrue(clzz.isInterface(), SpiExtensionException.class, clzz.getName() + "不是一个接口");
        Asserts.assertTrue(clzz.getAnnotation(SPI.class), SpiExtensionException.class, clzz.getName() + "不是SPI 接口");
        this.refClass = clzz;
        LOADER_MAP.put(clzz, this);
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return new ExtensionLoader<>(type);
    }

    public T getExtension(String name) {
        Holder<?> holder = NAME_OBJ_MAP.computeIfAbsent(name, k -> new Holder<>());
        if (holder.getValue() == null) {
            synchronized (holder) {
                if (holder.getValue() == null) {
                    Class<?> targetClass = getExtensionClasses().get(name);
                    Asserts.assertTrue(targetClass == null, SpiExtensionException.class, "查找不到" + name + "扩展");
                }
            }
        }
        return null;
    }

    private ConcurrentHashMap<String, Class<?>> getExtensionClasses() {
        if (inited) {
            return NAME_CLASS_MAP;
        }
        if(Unsafe.compareAndSwap(inited,,false,true)){

        }
    }

    public T getDefaultExtension() {
        return null;
    }

    public T getAdaptiveExtension() {
        return null;
    }

    @Data
    static class Holder<T> {
        T value;

    }
}
