package com.individual.remoting.api.transport.server;

import com.individual.remoting.api.channel.Channel;

import java.util.Collection;

public interface RemotingServer extends Server {


    /**
     * 获取与该服务端连接的客户端channels
     *
     * @return
     */
    Collection<Channel> getAlreadyClientChannels();
}
