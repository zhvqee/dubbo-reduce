package com.individual.common.extentions;

public class ExtensionLoader<T> {

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return new ExtensionLoader<>();
    }

    public T getExtension(String name) {
        return null;
    }

    public  T getDefaultExtension(){

    }

    public T getAdaptiveExtension() {
        return null;
    }
}
