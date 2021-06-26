package com.individual.remoting.api.channelHanlder;

import com.individual.common.exceptions.RemotingException;
import com.individual.remoting.api.channel.Channel;

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
