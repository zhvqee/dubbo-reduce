package org.qee.cloud.remoting.netty4.channel;


import org.qee.cloud.remoting.api.channel.Channel;

public class NettyChannel implements Channel {

    public static NettyChannel getOrAddChannel(io.netty.channel.Channel channel) {
        return new NettyChannel();
    }


    public static void removeChannelIfDisconnected(io.netty.channel.Channel channel) {
    }

    @Override
    public void close() {

    }
}
