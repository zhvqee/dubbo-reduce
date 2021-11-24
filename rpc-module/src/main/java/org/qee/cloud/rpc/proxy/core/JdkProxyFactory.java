package org.qee.cloud.rpc.proxy.core;

import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.InvokerInvocationHandler;
import org.qee.cloud.rpc.Result;
import org.qee.cloud.rpc.proxy.AbstractProxyFactory;

import java.lang.reflect.Proxy;
import java.util.Set;

public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {
        return (T) Proxy.newProxyInstance(invoker.getClass().getClassLoader(), interfaces.toArray(new Class[0]), (proxy, method, args) -> {
            Result result = invoker.invoke(new InvokerInvocationHandler<>(invoker, method.getName(), args, method.getReturnType()));
            return result.getValue();
        });
    }


}
