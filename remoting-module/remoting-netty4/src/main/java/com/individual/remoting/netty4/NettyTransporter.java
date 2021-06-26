package com.individual.remoting.netty4;

import com.individual.common.exceptions.RemotingException;
import com.individual.common.model.URL;
import com.individual.remoting.api.channelHanlder.ChannelHandler;
import com.individual.remoting.api.transport.Transporter;
import com.individual.remoting.api.transport.client.Client;
import com.individual.remoting.api.transport.server.RemotingServer;
import com.individual.remoting.netty4.client.Netty4Client;
import com.individual.remoting.netty4.server.Netty4Server;

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
