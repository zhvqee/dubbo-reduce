package org.qee.cloud.common.extentions;

import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

    private Class<T> refClass;


    private static ConcurrentHashMap<Class<?>,ExtensionLoader<?>> LOADER_MAP = new ConcurrentHashMap<>();


    private ExtensionLoader(Class<T> clzz) {
        this.refClass = clzz;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return new ExtensionLoader<>(type);
    }

    public T getExtension(String name) {
        return null;
    }

    public T getDefaultExtension() {
        return null;
    }

    public T getAdaptiveExtension() {
        return null;
    }


}
