package org.qee.cloud.remoting.api.transport.server;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.NetUtils;
import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;
import org.qee.cloud.remoting.api.transport.AbstractEndPoint;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抽象服务端，主要做TCP 层的抽象，主要包括如下内容：
 * <p>
 * 1、定义业务线程池 executor
 * <p>
 * 2、服务端本地地址 localAddress
 * <p>
 * <p>
 * 3、服务端binding端口，进行监听 bindAddress
 * <p>
 * 4、可接受的连接数 accepts
 * <p>
 * 5、我把ChannelHandler 放到抽象AbstractServer，个人认为服务端或者客户端模型则具有处理行为
 */
public abstract class AbstractServer extends AbstractEndPoint implements RemotingServer, ChannelHandler {

    protected static final String SERVER_THREAD_POOL_NAME = "DubboReduceServerHandler";


    private InetSocketAddress localAddress;

    private InetSocketAddress bindAddress;

    private int accepts;

    private int idleTimeout;

    private ChannelHandler channelHandler;

    private ExecutorService executor;

    public AbstractServer(URL url, ChannelHandler channelHandler) throws RemotingException {
        super(url);

        localAddress = getUrl().toInetSocketAddress();

        String bindIp = getUrl().getParameter(BIND_IP_KEY, getUrl().getHost());
        int bindPort = getUrl().getParameter(BIND_PORT_KEY, getUrl().getPort());

        if (NetUtils.isInvalidLocalHost(bindIp)) {
            bindIp = LOCALHOST_VALUE;
        }
        this.bindAddress = new InetSocketAddress(bindIp, bindPort);

        this.accepts = url.getParameter(ACCEPTS_KEY, DEFAULT_ACCEPTS);

        this.idleTimeout = url.getParameter(IDLE_TIMEOUT_KEY, DEFAULT_IDLE_TIMEOUT);
        this.channelHandler = channelHandler;


        this.executor = createExecutorIfAbsent(url);

        try {
            doOpen();
        } catch (Throwable e) {
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to bind " + getClass().getSimpleName()
                    + " on " + getLocalAddress() + ", cause: " + e.getMessage(), e);
        }

    }

    // TODO: 2021/6/25
    protected ExecutorService createExecutorIfAbsent(URL url) {
        return Executors.newFixedThreadPool(100);
    }

    @Override
    public void close() {
        executor.shutdownNow();

        try {
            super.close();
        } catch (Throwable e) {

        }

        try {
            doClose();
        } catch (Throwable e) {

        }
    }

    public abstract void doOpen() throws Throwable;


    public abstract void doClose() throws Throwable;

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    /**
     * 1、因为NettyServer 实现ChannelHandler，则把自己传给NettyServerHandler，而NettyServerHandler被netty调用，
     * NettyServerHandler 行为委派给了自己内部的channelHanlder（该handler 即是NettyServer）
     * <p>
     * 2、而NettyServer 的父类AbstractServer 维护了由上层调用方的传入的channelhandler,
     * 所以，该AbstractServer 的channelHandler 再次委派给上层业务方闯入的channelHandler.
     */
    @Override
    public void connected(Channel channel) throws RemotingException {
        if (isClosed()) {
            channel.close();
            return;
        }
        // 连接不是无限制的，业务框架上的限制，netty本身和系统也有其限制
        Collection<Channel> alreadyClientChannels = getAlreadyClientChannels();
        if (accepts > 0 && alreadyClientChannels.size() >= accepts) {
            close();
            return;
        }

        channelHandler.connected(channel);
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        this.close();
        channelHandler.connected(channel);
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {
        if (isClosed()) {
            return;
        }
        channelHandler.sent(channel, message);
    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {
        if (isClosed()) {
            return;
        }
        channelHandler.received(channel, message);
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        channelHandler.caught(channel, exception);
    }
}

