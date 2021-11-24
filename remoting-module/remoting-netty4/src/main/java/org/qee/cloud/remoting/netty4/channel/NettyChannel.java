package org.qee.cloud.remoting.netty4.channel;


import org.qee.cloud.remoting.api.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannel implements Channel {

    private io.netty.channel.Channel channel;

    public NettyChannel(io.netty.channel.Channel channel) {
        this.channel = channel;
    }

    private static final Map<io.netty.channel.Channel, NettyChannel> channelMap = new ConcurrentHashMap<>();

    public static NettyChannel getOrAddChannel(io.netty.channel.Channel channel) {
        return channelMap.computeIfAbsent(channel, NettyChannel::new);
    }


    public static void removeChannelIfDisconnected(io.netty.channel.Channel channel) {
        channelMap.remove(channel);
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void sent(Object msg) {
        channel.writeAndFlush(msg);
    }
}
