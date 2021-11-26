package org.qee.cloud.common.utils;

import org.qee.cloud.common.exceptions.CloudException;
import org.qee.cloud.common.exceptions.SpiExtensionException;

public class Throws {
    public static void throwException(Class<? extends CloudException> clzz, String errMsg) {
        try {
            throw clzz.getDeclaredConstructor(String.class).newInstance(errMsg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SpiExtensionException(errMsg);
        }
    }
}
