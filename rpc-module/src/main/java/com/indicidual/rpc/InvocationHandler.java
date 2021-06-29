package com.indicidual.rpc;

public interface InvocationHandler {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
