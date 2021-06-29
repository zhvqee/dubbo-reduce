package com.indicidual.rpc.proxy;

import com.indicidual.rpc.Invoker;

import java.net.URL;

public interface ProxyFactory {


    /**
     * 该接口一般运用于客户端，因为只有接口，所以需要动态创建一个 代理对象T
     *
     * @param invoker
     * @param <T>
     * @return
     */
    <T> T getProxy(Invoker<T> invoker);

    /**
     * 这个接口一般运用于服务端，把一个服务端的对象proxy 封装为一个Invoker ，通过invoker 来代理proxy
     *
     * @param proxy
     * @param url
     * @param <T>
     * @return
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url);
}
