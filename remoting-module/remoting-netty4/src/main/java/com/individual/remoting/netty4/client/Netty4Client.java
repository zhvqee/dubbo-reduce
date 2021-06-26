package com.individual.remoting.netty4.client;

import com.individual.common.model.URL;
import com.individual.remoting.api.channelHanlder.ChannelHandler;
import com.individual.remoting.api.transport.client.AbstractClient;

import java.net.InetSocketAddress;

public class Netty4Client extends AbstractClient {

    public Netty4Client(URL url, ChannelHandler channelHandler) {
        super(url);
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }
}
