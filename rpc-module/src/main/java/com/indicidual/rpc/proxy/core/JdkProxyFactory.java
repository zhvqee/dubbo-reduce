package com.indicidual.rpc.proxy.core;

import com.indicidual.rpc.InvocationHandler;
import com.indicidual.rpc.Invoker;
import com.indicidual.rpc.InvokerInvocationHandler;
import com.indicidual.rpc.Result;
import com.indicidual.rpc.proxy.AbstractProxyFactory;
import com.indicidual.rpc.proxy.AbstractProxyInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Set;

public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {

        return (T) Proxy.newProxyInstance(invoker.getClass().getClassLoader(), (Class<?>[]) interfaces.toArray(), (proxy, method, args) -> {
            Result result = invoker.invoke(new InvokerInvocationHandler(invoker));
            return result.getValue();
        });
    }


}
