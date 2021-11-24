package org.qee.cloud.common.utils;

import java.lang.reflect.Method;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.utils
 * @ClassName: MethodReturnUtils
 * @Description:
 * @Date: 2021/11/24 10:18 上午
 * @Version: 1.0
 */
public class MethodReturnUtils {

    public static boolean isVoidReturn(Method method) {
        if (method.getReturnType() == Void.class || method.getReturnType() == void.class) {
            return true;
        }
        return false;
    }

    public static boolean isVoidClass(Class<?> clzz) {
        if (clzz == Void.class || clzz == void.class) {
            return true;
        }
        return false;
    }
}
