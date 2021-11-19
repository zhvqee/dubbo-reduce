package org.qee.cloud.remoting.api.transport;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.transport.client.Client;
import org.qee.cloud.remoting.api.transport.server.RemotingServer;

/**
 * 主要是把 服务端 和客户端 统一起来，称之为传输器
 */
public interface Transporter {


    /**
     * 绑定创建一个服务端，这里默认采用nettyServer
     *
     * @return
     */
    RemotingServer bind(URL url, ChannelHandler channelHandler) throws RemotingException;


    Client connect(URL url, ChannelHandler channelHandler) throws RemotingException;
}
