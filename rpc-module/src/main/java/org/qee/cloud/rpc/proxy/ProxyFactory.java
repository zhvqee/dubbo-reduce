package org.qee.cloud.rpc.proxy;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.rpc.Invoker;

import java.net.URL;

/**
 * 为了让 客户端像调用本地方法一样，远程调用服务，需要有一个代理工程
 * 2、客户端通过getProxy 获得一个代理
 * 服务端的实例T 封装为Invoker ，进行远程应答
 */
@SPI(name = "buddy")
public interface ProxyFactory {


    /**
     * 该接口一般运用于客户端，因为只有接口，所以需要动态创建一个 代理对象T
     *
     * @param invoker
     * @param <T>
     * @return
     */
    @Adaptive(keys = "buddy")
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
