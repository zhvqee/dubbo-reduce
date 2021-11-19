package org.qee.cloud.remoting.netty4.client;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.transport.client.AbstractClient;

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
