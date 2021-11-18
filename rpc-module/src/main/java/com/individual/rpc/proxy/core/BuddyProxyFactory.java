package com.individual.rpc.proxy.core;

import com.individual.common.exceptions.ProxyException;
import com.individual.rpc.Invoker;
import com.individual.rpc.InvokerInvocationHandler;
import com.individual.rpc.proxy.AbstractProxyFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BuddyProxyFactory extends AbstractProxyFactory {

    /**
     * 客户端
     *
     * @param invoker
     * @param interfaces
     * @param <T>
     * @return
     */
    @Override
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {
        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
        Object newInstance = null;
        try {
            newInstance = byteBuddy.subclass(Object.class, ConstructorStrategy.Default.IMITATE_SUPER_CLASS)
                    .implement(new ArrayList<>(interfaces))
                    .name(invoker.getInterface().getName() + "ImplProxy")
                    .method(getMatcherMethod(interfaces))
                    .intercept(InvocationHandlerAdapter.of(new RpcInvocationHandler<>(invoker)))
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProxyException("代理" + invoker.getInterface().getName() + "异常");
        }

        return (T) newInstance;
    }

    private static ElementMatcher<? super MethodDescription> getMatcherMethod(Set<Class<?>> interfaces) {
        ElementMatcher.Junction<MethodDescription> elementMatcher = ElementMatchers.none();
        for (Class clzz : interfaces) {
            elementMatcher = elementMatcher.or(ElementMatchers.isDeclaredBy(clzz));
        }
        return elementMatcher;
    }


    static class RpcInvocationHandler<T> implements InvocationHandler {

        private Invoker<T> invoker;

        public RpcInvocationHandler(Invoker<T> invoker) {
            this.invoker = invoker;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            InvokerInvocationHandler<T> invocationHandler = new InvokerInvocationHandler<>(invoker, method.getName(), args);
            return invoker.invoke(invocationHandler);
        }
    }
}
