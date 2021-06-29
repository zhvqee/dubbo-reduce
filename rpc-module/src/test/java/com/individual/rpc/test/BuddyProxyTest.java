package com.individual.rpc.test;

import com.indicidual.rpc.InvocationHandler;
import com.indicidual.rpc.Invoker;
import com.indicidual.rpc.proxy.AbstractProxyInvoker;
import com.indicidual.rpc.proxy.ProxyFactory;
import com.indicidual.rpc.proxy.core.BuddyProxyFactory;
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
        });

    }



    @Test
    public  void testGetProxy(){
        ProxyFactory proxyFactory = new BuddyProxyFactory();
     //   Invoker<Op> invoker = proxyFactory.getInvoker(new User("jame", 31), Op.class, null);

        Op proxyFactoryProxy = proxyFactory.getProxy(new AbstractProxyInvoker<Op>() {
            @Override
            protected Object doInvoke(InvocationHandler invocationHandler) {
                return null;
            }

            @Override
            public Class<Op> getInterface() {
                return Op.class;
            }
        });
        proxyFactoryProxy.print();
        proxyFactoryProxy.compute(9,5);
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
