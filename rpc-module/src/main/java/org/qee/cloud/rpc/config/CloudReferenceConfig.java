package org.qee.cloud.rpc.config;

import lombok.Data;
import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.rpc.InvocationHandler;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.proxy.AbstractProxyInvoker;
import org.qee.cloud.rpc.proxy.ProxyFactory;

@Data
public class CloudReferenceConfig<T> {

    private String id;

    private String interfaceName;

    private Class<T> interfaceClass;

    private String version;

    private String group;

    private String mock;

    private boolean check;

    private T ref;

    private boolean inited;

    private ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

    public T get() {
        if (inited && ref != null) {
            return ref;
        }

        Invoker<T> invoker = new AbstractProxyInvoker<T>() {
            @Override
            protected Object doInvoke(InvocationHandler invocationHandler) {
                // TODO: 2021/11/21  待做 
                System.out.println("12233434343");
                return null;
            }

            @Override
            public Class<T> getInterface() {
                return interfaceClass;
            }
        };
        ref = proxyFactory.getProxy(invoker);
        return ref;
    }
}
