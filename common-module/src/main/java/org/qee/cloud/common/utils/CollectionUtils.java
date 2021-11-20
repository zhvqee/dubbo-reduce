package org.qee.cloud.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static <K extends Comparable<K>, V extends Comparable<V>> LinkedHashMap<K, V> sortMap(Map<K, V> asMap) {
        LinkedHashMap<K, V> ret = new LinkedHashMap<>();
        asMap.entrySet().stream().sorted((m1, m2) -> m2.getValue().compareTo(m1.getValue()))
                .collect(Collectors.toList()).forEach(entry -> ret.put(entry.getKey(), entry.getValue()));
        return ret;
    }


    public static <V extends Comparable<V>> LinkedHashMap<?, V> sortMap2(Map<?, V> asMap) {
        LinkedHashMap<Object, V> ret = new LinkedHashMap<>();
        asMap.entrySet().stream().sorted((m1, m2) -> m2.getValue().compareTo(m1.getValue()))
                .collect(Collectors.toList()).forEach(entry -> ret.put(entry.getKey(), entry.getValue()));
        return ret;
    }
}
