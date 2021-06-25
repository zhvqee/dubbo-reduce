package com.individual.remoting.api.transport;

import com.individual.common.model.URL;

import java.net.InetSocketAddress;

public interface EndPoint {
    /**
     * 描述端点的url
     *
     * @return
     */
    URL getUrl();

    /**
     * 端点地址
     *
     * @return
     */
    InetSocketAddress getLocalAddress();


    /**
     * 端是否关闭
     *
     * @return
     */
    boolean isClosed();


    /**
     * 关闭
     */
    void close();
}
