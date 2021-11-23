package org.qee.cloud.remoting.netty4.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.remoting.api.transport.client.AbstractClient;
import org.qee.cloud.remoting.netty4.codec.NettyCodecAdapter;
import org.qee.cloud.remoting.netty4.nettyhandler.NettyHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Netty4Client extends AbstractClient {

    private Bootstrap bootstrap;

    private io.netty.channel.Channel channel;

    private ChannelHandler channelHandler;

    /**
     * 这里维护 该应用下所有的服务端连接
     */
    private static Map<String, Channel> clientChannelMap = new ConcurrentHashMap<>();


    private static final EventLoopGroup NIO_EVENT_LOOP_GROUP = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
        private AtomicInteger id = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("NettyClientWorker-" + id.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    });


    public Netty4Client(URL url, ChannelHandler channelHandler) {
        super(url);
        this.channelHandler = channelHandler;
        try {
            doConnect();
        } catch (InterruptedException e) {
            Throws.throwException(RemotingException.class, "远程连接失败,providerUrl:" + url);
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public CompletableFuture<Response> request(Request request) {
        return null;
    }

    @Override
    protected void doOpen() {
        bootstrap = new Bootstrap();
        NettyHandler nettyHandler = new NettyHandler(channelHandler, clientChannelMap);
        bootstrap.group(NIO_EVENT_LOOP_GROUP)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.max(3000, getConnectTimeout()))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec());
                        socketChannel.pipeline()
                                .addLast("decoder", adapter.getDecoder())
                                .addLast("encoder", adapter.getEncoder())
                                //.addLast("server-idle-handler", new IdleStateHandler(0, 0, idleTimeout, MILLISECONDS))
                                .addLast("handler", nettyHandler);
                    }
                });
    }

    @Override
    protected void doConnect() throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(getUrl().getHost(), getUrl().getPort());
        boolean uninterruptibly = channelFuture.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);
        if (uninterruptibly && channelFuture.isSuccess()) {
            if (Netty4Client.this.channel != null) {
                Netty4Client.this.channel.close();
            }
            this.channel = channelFuture.channel();

        } else {
            Throws.throwException(RemotingException.class, "远程连接异常,providerUrl：" + getUrl());
        }

    }
}
