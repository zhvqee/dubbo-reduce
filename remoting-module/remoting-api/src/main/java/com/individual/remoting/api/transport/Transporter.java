package com.individual.remoting.api.transport;

import com.individual.common.exceptions.RemotingException;
import com.individual.common.model.URL;
import com.individual.remoting.api.channelHanlder.ChannelHandler;
import com.individual.remoting.api.transport.client.Client;
import com.individual.remoting.api.transport.server.RemotingServer;

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
