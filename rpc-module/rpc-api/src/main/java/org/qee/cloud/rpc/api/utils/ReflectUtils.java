package org.qee.cloud.rpc.api.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectUtils {

    /**
     * void(V).
     */
    public static final char JVM_VOID = 'V';

    /**
     * boolean(Z).
     */
    public static final char JVM_BOOLEAN = 'Z';

    /**
     * byte(B).
     */
    public static final char JVM_BYTE = 'B';

    /**
     * char(C).
     */
    public static final char JVM_CHAR = 'C';

    /**
     * double(D).
     */
    public static final char JVM_DOUBLE = 'D';

    /**
     * float(F).
     */
    public static final char JVM_FLOAT = 'F';

    /**
     * int(I).
     */
    public static final char JVM_INT = 'I';

    /**
     * long(J).
     */
    public static final char JVM_LONG = 'J';

    /**
     * short(S).
     */
    public static final char JVM_SHORT = 'S';


    public static ConcurrentHashMap<String, Method> signatureMap = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Class<?>> descClassMap = new ConcurrentHashMap<>();


    public static class User {

    }

    public static class TestDesc {

        public void getMap(Map<String, List<User>> mp) {

        }

        public void getArr(User[] users) {

        }

        public void primary(boolean[] ba) {

        }
    }

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
        Method[] declaredMethods = TestDesc.class.getDeclaredMethods();
        for (Method md : declaredMethods) {

            for (Class<?> pt : md.getParameterTypes()) {
                String desc = getDesc(pt);

                System.out.println(desc);


                Method method = TestDesc.class.getMethod(md.getName(), pt);
                System.out.println(method);

                System.out.println("======");
                method = TestDesc.class.getMethod(md.getName(), getClass(desc));
                System.out.println(method);

            }
        }
    }

    public static Class<?> getClass(String desc) throws ClassNotFoundException {
        Class<?> clzz = descClassMap.get(desc);
        if (clzz != null) {
            return clzz;
        }
        if (desc.startsWith("L")) {
            clzz = Class.forName(desc.substring(1, desc.length() - 1).replace("/", "."));
            descClassMap.put(desc, clzz);
            return clzz;
        }
        if (desc.startsWith("[")) {
            clzz = Class.forName(desc.replace("/", "."));
            descClassMap.put(desc, clzz);
            return clzz;
        }
        char c = desc.charAt(0);
        switch (c) {
            case JVM_VOID:
                return void.class;
            case JVM_BOOLEAN:
                return boolean.class;
            case JVM_BYTE:
                return byte.class;
            case JVM_CHAR:
                return char.class;
            case JVM_DOUBLE:
                return double.class;
            case JVM_FLOAT:
                return float.class;
            case JVM_INT:
                return int.class;
            case JVM_LONG:
                return long.class;
            case JVM_SHORT:
                return short.class;
        }
        throw new IllegalArgumentException("非法描述:" + desc);
    }

    public static String getDesc(Class<?> c) {
        StringBuilder ret = new StringBuilder();

        while (c.isArray()) {
            ret.append('[');
            c = c.getComponentType();
        }

        if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append(JVM_VOID);
            } else if ("boolean".equals(t)) {
                ret.append(JVM_BOOLEAN);
            } else if ("byte".equals(t)) {
                ret.append(JVM_BYTE);
            } else if ("char".equals(t)) {
                ret.append(JVM_CHAR);
            } else if ("double".equals(t)) {
                ret.append(JVM_DOUBLE);
            } else if ("float".equals(t)) {
                ret.append(JVM_FLOAT);
            } else if ("int".equals(t)) {
                ret.append(JVM_INT);
            } else if ("long".equals(t)) {
                ret.append(JVM_LONG);
            } else if ("short".equals(t)) {
                ret.append(JVM_SHORT);
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

}
