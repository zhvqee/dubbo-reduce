package org.qee.cloud.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;

public class Reflects {

    public static boolean isSetter(Method method) {
        if (method.getName().startsWith("set") && method.getParameters().length == 1
                && method.getReturnType() == void.class) {

            return true;
        }
        return false;

    }

    public static boolean isPrimitives(Class<?> parameterType) {
        return parameterType.isArray() ? isPrimitive(parameterType.getComponentType()) : isPrimitive(parameterType);
    }


    private static boolean isPrimitive(Class<?> parameterType) {
        return isWrapper(parameterType) ||
                parameterType.isPrimitive()
                || parameterType == String.class
                || parameterType == Boolean.class
                || parameterType == Character.class
                || Number.class.isAssignableFrom(parameterType)
                || Date.class.isAssignableFrom(parameterType);
    }

    private static boolean isWrapper(Class<?> parameterType) {
        return parameterType == Integer.class
                || parameterType == Long.class
                || parameterType == Short.class
                || parameterType == Double.class
                || parameterType == Byte.class
                || parameterType == Float.class
                || parameterType == Character.class;
    }

    public static String getDefaultFieldName(Method method) {
        return StringUtils.lowerCase(StringUtils.substring(method.getName(), 3, 1)) + StringUtils.substring(method.getName(), 4);
    }

    public static void main(String[] args) {
        String  ss="setA";
        String s = StringUtils.lowerCase(StringUtils.substring(ss, 3, 4)) + StringUtils.substring(ss, 4);
        System.out.println(s);

    }
}
