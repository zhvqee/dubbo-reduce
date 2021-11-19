package org.qee.cloud.rpc;

public interface Invoker<T> {

    Class<T> getInterface();


    Result invoke(InvocationHandler invocationHandler);
}
