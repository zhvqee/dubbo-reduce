package com.individual.rpc;

public interface Invoker<T> {

    Class<T> getInterface();


    Result invoke(InvocationHandler invocationHandler);
}
