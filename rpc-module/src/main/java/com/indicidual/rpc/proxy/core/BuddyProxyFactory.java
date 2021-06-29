package com.indicidual.rpc.proxy.core;

import com.indicidual.rpc.Invoker;
import com.indicidual.rpc.InvokerInvocationHandler;
import com.indicidual.rpc.proxy.AbstractProxyFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BuddyProxyFactory extends AbstractProxyFactory {
    @Override
    protected <T> T getProxy(Invoker<T> invoker, Set<Class<?>> interfaces) {

        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
        List<Class<?>> classList = new ArrayList<>(interfaces);
        interfaces.remove(invoker.getInterface());
        DynamicType.Unloaded<T> unloaded = byteBuddy.with(new NamingStrategy.AbstractBase() {

            @Override
            protected String name(TypeDescription superClass) {
                return superClass.getName() + "Proxy";
            }
        }).subclass(invoker.getInterface())
                .implement(new ArrayList<>(interfaces))
                .method(ElementMatchers.any())
              //  .intercept(In)
                .intercept(MethodDelegation.to(new BuddyInteceptor<>(invoker)))
                .make();
        Class<? extends T> proxyClass = unloaded.load(invoker.getClass().getClassLoader()).getLoaded();

        Object newInstance = null;
        try {
            newInstance = proxyClass.newInstance();
        } catch (Throwable e) {

        }
        return (T) newInstance;
    }

    private static class BuddyInteceptor<T> {
        private Invoker<T> invoker;

        public BuddyInteceptor(Invoker<T> invoker) {
            this.invoker = invoker;
        }


        @RuntimeType
        public Object interceptor(@This Object proxy, @Origin Method method,
                                  @AllArguments Object[] args) {
            InvokerInvocationHandler<T> invokerInvocationHandler = new InvokerInvocationHandler<>(invoker);
            invokerInvocationHandler.setMethodName(method.getName());
            invokerInvocationHandler.setArguments(args);
            invokerInvocationHandler.setParameterTypes(method.getParameterTypes());
            return invoker.invoke(invokerInvocationHandler);
        }
    }
}
