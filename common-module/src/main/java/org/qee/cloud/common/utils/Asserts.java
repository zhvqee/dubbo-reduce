package org.qee.cloud.common.utils;

import org.qee.cloud.common.exceptions.CloudException;

import java.util.Collection;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.utils
 * @ClassName: Asserts
 * @Description:
 * @Date: 2021/11/19 4:59 下午
 * @Version: 1.0
 */
public class Asserts {

    @lombok.SneakyThrows
    public static void assertTrue(boolean condition, Class<? extends CloudException> clzz, String errMsg) {
        if (!condition) {
            throw clzz.getDeclaredConstructor(String.class).newInstance(errMsg);
        }
    }

    @lombok.SneakyThrows
    public static void assertTrue(Object condition, Class<? extends CloudException> clzz, String errMsg) {
        if (condition == null) {
            throw clzz.getDeclaredConstructor(String.class).newInstance(errMsg);
        }
    }

    @lombok.SneakyThrows
    public static void assertTrue(Collection condition, Class<? extends CloudException> clzz, String errMsg) {
        if (condition == null || condition.isEmpty()) {
            throw clzz.getDeclaredConstructor(String.class).newInstance(errMsg);
        }
    }
}
