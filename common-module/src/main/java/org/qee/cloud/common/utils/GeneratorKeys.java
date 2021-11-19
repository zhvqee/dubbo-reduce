package org.qee.cloud.common.utils;

import java.util.concurrent.atomic.AtomicLong;

public class GeneratorKeys {

    private static AtomicLong id = new AtomicLong();

    public static String generate(String preName) {
        return preName + id.getAndIncrement();
    }
}
