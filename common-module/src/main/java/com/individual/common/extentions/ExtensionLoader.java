package com.individual.common.extentions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExtensionLoader<T> {

    private Class<T> type;

    private static final Map<Class<?>, ExtensionLoader<?>> clazzLoader = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();


    private ExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type ==null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type need interface");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("type class is annotation @spi");
        }
        this.type = type;
        clazzLoader.putIfAbsent(type, this);
    }


    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return new ExtensionLoader<>(type);
    }


    public T getExtension(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (name.equals("true") || name.equals("default")) {
            return getDefaultExtension();
        }

        Holder<Object> holder = getOrCreateHolder(name);
        if (holder.getValue() == null) {
            synchronized (holder) {
                if (holder.getValue() == null) {
                    createExtension(name, holder);
                }
            }
        }
        return (T) holder.getValue();
    }

    private void createExtension(String name, Holder<Object> holder) {
        Class clazz = getExtensionClass(name);
        if (clazz == null) {
            throw new IllegalArgumentException("illegal name");
        }

    }

    private Class getExtensionClass(String name) {
        Class<?> clazz = cachedClasses.get(name);
        if (clazz == null) {
            synchronized (cachedClasses) {
                if (cachedClasses.get(name) == null) {
                    clazz = loadExtensionClass(name);
                    cachedClasses.put(name, clazz);
                }
            }
        }
        return clazz;
    }

    private Class<?> loadExtensionClass(String name) {

        return null;
    }

    private Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
        }
        return cachedInstances.get(name);
    }

    public T getDefaultExtension() {
        return null;
    }

    public T getAdaptiveExtension() {
        return null;
    }


    static class Holder<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
