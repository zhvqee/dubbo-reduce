package org.qee.cloud.remoting.api.channelHanlder;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.remoting.api.channel.Channel;

public abstract class AbstractChannelHandlerDelegate implements ChannelHandler {

    private ChannelHandler channelHandler;

    public AbstractChannelHandlerDelegate(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void connected(Channel channel) throws RemotingException {

    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {

    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {

    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {

    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {

    }
}
