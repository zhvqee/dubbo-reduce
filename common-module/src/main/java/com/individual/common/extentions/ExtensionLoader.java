package com.individual.common.extentions;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

    private Class<T> refClass;


    private static ConcurrentHashMap<Class<?>,ExtensionLoader<?>> LOADER_MAP = new Co<>();


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
