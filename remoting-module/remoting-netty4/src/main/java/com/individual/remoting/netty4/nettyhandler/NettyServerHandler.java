package com.individual.remoting.netty4.nettyhandler;

import com.individual.common.utils.NetUtils;
import com.individual.remoting.api.channel.Channel;
import com.individual.remoting.api.channelHanlder.ChannelHandler;
import com.individual.remoting.netty4.channel.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * 一个netty tcp 连接(一个nettyServer) 维护一个NettyServerHandler 对象
 * <p>
 * 1、该对象处理维护业务的channelHandler 处理外
 * <p>
 * <p>
 * 2、维护了这个与这个服务端连接的对等客户端的channel通道，即通过map维护tcp 链接 即 1（一个服务端） : n （多个客户端）关系
 */
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    /**
     * 业务channel handler
     */
    private ChannelHandler channelHandler;

    /**
     * 维护了与这个服务端连接的客户端channel. 其中 key-> host:port ,value:NettyChannel
     */
    private final Map<String, Channel> clientChannelMap;


    public NettyServerHandler(ChannelHandler channelHandler, Map<String, Channel> clientChannelMap) {
        if (channelHandler == null) {
            throw new IllegalArgumentException("NettyServerHandler channelHandler is null");
        }
        this.channelHandler = channelHandler;
        this.clientChannelMap = clientChannelMap;
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        channelHandler.sent(NettyChannel.getOrAddChannel(ctx.channel()), msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String hostKey = NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress());
        NettyChannel nettyChannel = NettyChannel.getOrAddChannel(ctx.channel());
        clientChannelMap.put(hostKey, nettyChannel);
        channelHandler.connected(nettyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getOrAddChannel(ctx.channel());
        try {
            channelHandler.disconnected(nettyChannel);
        } finally {
            // 本地移除这个channel
            clientChannelMap.remove(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelHandler.received(NettyChannel.getOrAddChannel(ctx.channel()), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
        try {
            // 异常业务处理
            channelHandler.caught(channel, cause);
        } finally {
            // 本地移除这个channel
            clientChannelMap.remove(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }
}
