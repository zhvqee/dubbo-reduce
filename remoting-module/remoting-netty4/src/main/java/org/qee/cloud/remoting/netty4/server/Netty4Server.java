package org.qee.cloud.remoting.netty4.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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


        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(EpollChannelOption.SO_REUSEPORT, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
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

        channel = serverBootstrap.bind(getUrl().getPort()).syncUninterruptibly().channel();
        // channel.closeFuture().sync();
    }

    @Override
    public void doClose() throws Throwable {
        if (channel != null) {
            channel.close();
            System.out.println("close");
        }
        if (boss != null) {
            boss.shutdownGracefully();
            System.out.println("close");
        }
        if (worker != null) {
            worker.shutdownGracefully();
            System.out.println("close");
        }
        clientChannelMap.clear();
    }


    @Override
    public Collection<Channel> getAlreadyClientChannels() {
        return clientChannelMap.values();
    }
}
