package com.individual.remoting.api.transport;

import com.individual.common.exceptions.RemotingException;
import com.individual.common.extentions.ExtensionLoader;
import com.individual.common.model.URL;
import com.individual.remoting.api.channelHanlder.ChannelHandler;
import com.individual.remoting.api.transport.client.Client;
import com.individual.remoting.api.transport.server.RemotingServer;

/**
 * 上层exchange 与transporter 层打交道入口
 */
public class Transporters {
    /**
     * 绑定创建一个服务端，这里默认采用nettyServer
     *
     * @return
     */
    public static RemotingServer bind(URL url, ChannelHandler channelHandler) throws RemotingException {
        return getTransporter(url).bind(url, channelHandler);
    }


    public static Client connect(URL url, ChannelHandler channelHandler) throws RemotingException {
        return getTransporter(url).connect(url, channelHandler);
    }

    public static Transporter getTransporter(URL url) {
        return ExtensionLoader.getExtensionLoader(Transporter.class).getDefaultExtension(); //目前只给netty，
    }


}
