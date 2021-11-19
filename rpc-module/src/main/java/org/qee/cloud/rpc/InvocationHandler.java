package org.qee.cloud.rpc;

public interface InvocationHandler {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
