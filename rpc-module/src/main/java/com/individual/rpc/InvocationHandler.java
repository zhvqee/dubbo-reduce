package com.individual.rpc;

public interface InvocationHandler {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
