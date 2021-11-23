package org.qee.cloud.remoting.netty4.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandlers;
import org.qee.cloud.remoting.api.transport.server.AbstractServer;
import org.qee.cloud.remoting.netty4.codec.NettyCodecAdapter;
import org.qee.cloud.remoting.netty4.nettyhandler.NettyHandler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.qee.cloud.common.constants.CommonConstants.DEFAULT_IO_THREADS;
import static org.qee.cloud.common.constants.CommonConstants.IO_THREADS_KEY;

public class Netty4Server extends AbstractServer {


    private ServerBootstrap serverBootstrap;

    private EventLoopGroup boss;

    private EventLoopGroup worker;
    //Netty 底层channel
    private io.netty.channel.Channel channel;

    private Map<String, Channel> clientChannelMap;

    /**
     * 这里由上层的 传入ChannelHandler.
     * 对应上层的传入的channelhandler ，我们可以对其进行一些封装，包裹,利用ChannelHandlers.wrap
     *
     * @param url
     * @param channelHandler
     * @throws RemotingException
     */
    public Netty4Server(URL url, ChannelHandler channelHandler) throws RemotingException {
        super(url, ChannelHandlers.wrap(channelHandler, url));
    }

    @Override
    public void doOpen() throws Throwable {
        boss = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        worker = new NioEventLoopGroup(getUrl().getParameter(IO_THREADS_KEY, DEFAULT_IO_THREADS), new DefaultThreadFactory("NettyServerWorker", true));
        serverBootstrap = new ServerBootstrap();
        clientChannelMap = new ConcurrentHashMap<>();
        NettyHandler nettyServerHandler = new NettyHandler(Netty4Server.this, clientChannelMap);

        serverBootstrap.channel(NioServerSocketChannel.class)
                .group(boss, worker)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec());
                        ch.pipeline()
                                .addLast("decoder", adapter.getDecoder())
                                .addLast("encoder", adapter.getEncoder())
                                //.addLast("server-idle-handler", new IdleStateHandler(0, 0, idleTimeout, MILLISECONDS))
                                .addLast("handler", nettyServerHandler);
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(getBindAddress());
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    @Override
    public void doClose() throws Throwable {
        if (channel != null) {
            channel.close();
        }
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
        clientChannelMap.clear();
    }


    @Override
    public Collection<Channel> getAlreadyClientChannels() {
        return clientChannelMap.values();
    }
}
