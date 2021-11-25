package org.qee.cloud.rpc.api;

public interface InvocationHandler {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

    Class<?> getReturnType();
}
