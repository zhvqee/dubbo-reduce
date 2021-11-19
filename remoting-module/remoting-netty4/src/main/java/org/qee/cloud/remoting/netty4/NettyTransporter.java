package org.qee.cloud.remoting.netty4;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.transport.Transporter;
import org.qee.cloud.remoting.api.transport.client.Client;
import org.qee.cloud.remoting.api.transport.server.RemotingServer;
import org.qee.cloud.remoting.netty4.client.Netty4Client;
import org.qee.cloud.remoting.netty4.server.Netty4Server;

public class NettyTransporter implements Transporter {

    @Override
    public RemotingServer bind(URL url, ChannelHandler channelHandler) throws RemotingException {
        return new Netty4Server(url, channelHandler);
    }

    @Override
    public Client connect(URL url, ChannelHandler channelHandler) throws RemotingException {
        return new Netty4Client(url, channelHandler);
    }
}
