package org.qee.cloud.rpc.api.proxy.core;


import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.InvokerInvocationHandler;
import org.qee.cloud.rpc.api.Result;
import org.qee.cloud.rpc.api.proxy.AbstractProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Set;

public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {
        return (T) Proxy.newProxyInstance(invoker.getClass().getClassLoader(), interfaces.toArray(new Class[0]), (proxy, method, args) -> {
            Result result = invoker.invoke(new InvokerInvocationHandler<>(invoker, method, args, method.getReturnType()));
            return result.getValue();
        });
    }


}
