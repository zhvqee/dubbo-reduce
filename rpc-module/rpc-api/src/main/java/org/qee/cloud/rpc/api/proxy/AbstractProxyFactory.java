package org.qee.cloud.rpc.api.proxy;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.InvocationHandler;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.defaults.inters.EchoService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractProxyFactory implements ProxyFactory {

    private List<Class<?>> defaultInterface = Arrays.asList(EchoService.class);

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        Set<Class<?>> interfaces = new HashSet<>();
        interfaces.addAll(Arrays.asList(invoker.getInterface()));
        interfaces.addAll(defaultInterface);
        return getProxy(invoker, interfaces);
    }


    protected abstract <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces);


    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {

        return new AbstractProxyInvoker<T>() {

            @Override
            public URL getUrl() {
                return url;
            }

            @Override
            public Class<T> getInterface() {
                return type;
            }

            @Override
            protected Object doInvoke(InvocationHandler invocationHandler) {
                try {
                    Method method = proxy.getClass().getMethod(invocationHandler.getMethodName(), invocationHandler.getParameterTypes());
                    method.setAccessible(true);
                    return method.invoke(proxy, invocationHandler.getArguments());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
                return null;
            }
        };
    }
}
