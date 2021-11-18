package com.individual.rpc.proxy.core;

import com.individual.rpc.Invoker;
import com.individual.rpc.InvokerInvocationHandler;
import com.individual.rpc.Result;
import com.individual.rpc.proxy.AbstractProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Set;

public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {

        return (T) Proxy.newProxyInstance(invoker.getClass().getClassLoader(), interfaces.toArray(new Class[0]), (proxy, method, args) -> {
            Result result = invoker.invoke(new InvokerInvocationHandler(invoker, method.getName(), args));
            return result.getValue();
        });
    }


}
