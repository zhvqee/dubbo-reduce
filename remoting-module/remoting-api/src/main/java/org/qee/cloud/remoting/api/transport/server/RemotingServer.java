package org.qee.cloud.remoting.api.transport.server;

import org.qee.cloud.remoting.api.channel.Channel;

import java.util.Collection;

public interface RemotingServer extends Server {


    /**
     * 获取与该服务端连接的客户端channels
     *
     * @return
     */
    Collection<Channel> getAlreadyClientChannels();
}
