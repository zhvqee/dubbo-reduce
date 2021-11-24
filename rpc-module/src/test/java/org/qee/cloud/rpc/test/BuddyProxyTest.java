package org.qee.cloud.rpc.test;

import org.qee.cloud.rpc.InvocationHandler;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.proxy.AbstractProxyInvoker;
import org.qee.cloud.rpc.proxy.ProxyFactory;
import org.qee.cloud.rpc.proxy.core.BuddyProxyFactory;
import org.qee.cloud.rpc.proxy.core.JdkProxyFactory;
import org.junit.Test;

public class BuddyProxyTest {


    @Test
    public void testGetInvoker() {
        ProxyFactory proxyFactory = new BuddyProxyFactory();

        Invoker<Op> invoker = proxyFactory.getInvoker(new User("jam", 3), Op.class, null);
        invoker.invoke(new InvocationHandler() {
            @Override
            public String getMethodName() {
                return "print";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }

            @Override
            public Class<?> getReturnType() {
                return null;
            }
        });

        invoker.invoke(new InvocationHandler() {
            @Override
            public String getMethodName() {
                return "compute";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[]{int.class, int.class};
            }

            @Override
            public Object[] getArguments() {
                return new Object[]{1, 2};
            }

            @Override
            public Class<?> getReturnType() {
                return null;
            }
        });

    }


    @Test
    public void testGetBuddyProxy() {
        ProxyFactory proxyFactory = new BuddyProxyFactory();
        //   Invoker<Op> invoker = proxyFactory.getInvoker(new User("jame", 31), Op.class, null);

        Op proxyFactoryProxy = proxyFactory.getProxy(new AbstractProxyInvoker<Op>() {
            @Override
            protected Object doInvoke(InvocationHandler invocationHandler) {
                System.out.println(invocationHandler.getMethodName() + ":" + invocationHandler.getArguments()
                        + "：" + invocationHandler.getArguments());
                return null;
            }

            @Override
            public Class<Op> getInterface() {
                return Op.class;
            }
        });
        proxyFactoryProxy.print();
        proxyFactoryProxy.compute(9, 5);
        System.out.println(proxyFactoryProxy.toString());
        System.out.println(proxyFactoryProxy.hashCode());
    }

    @Test
    public void testGetJDKProxy() {
        ProxyFactory proxyFactory = new JdkProxyFactory();
        //   Invoker<Op> invoker = proxyFactory.getInvoker(new User("jame", 31), Op.class, null);

        Op proxyFactoryProxy = proxyFactory.getProxy(new AbstractProxyInvoker<Op>() {
            @Override
            protected Object doInvoke(InvocationHandler invocationHandler) {
                System.out.println(invocationHandler.getMethodName() + ":" + invocationHandler.getArguments()
                        + "：" + invocationHandler.getArguments());
                return null;
            }

            @Override
            public Class<Op> getInterface() {
                return Op.class;
            }
        });
        proxyFactoryProxy.print();
        proxyFactoryProxy.compute(9, 5);
    }

    interface Op {
        void print();

        void compute(int a, int b);
    }

    static class User implements Op {

        private String name;

        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public User() {
        }

        @Override
        public void print() {
            System.out.println(name + ": " + age);
        }

        @Override
        public void compute(int a, int b) {
            System.out.println(name + "'s age is " + age + " , compute add(" + a + "," + b + ")=" + (a + b));
        }
    }
}
