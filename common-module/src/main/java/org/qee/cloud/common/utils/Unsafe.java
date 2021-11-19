package org.qee.cloud.common.utils;

import java.lang.reflect.Field;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.utils
 * @ClassName: Unsafe
 * @Description:
 * @Date: 2021/11/19 5:47 下午
 * @Version: 1.0
 */
public class Unsafe {

    private static sun.misc.Unsafe theUnsafe;

    static {
        try {
            Field declaredField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            theUnsafe = (sun.misc.Unsafe) declaredField.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static boolean compareAndSwap(Object obj, long offset, boolean originValue, boolean updateValue) {
        return theUnsafe.compareAndSwapObject(obj, offset, originValue, updateValue);
    }

    public static long getObjectOffset(Field field) {
        return theUnsafe.objectFieldOffset(field);
    }

}
