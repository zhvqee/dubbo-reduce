package com.individual.remoting.api.exchange;

import com.individual.remoting.api.channelHanlder.AbstractChannelHandlerDelegate;
import com.individual.remoting.api.channelHanlder.ChannelHandler;

public class DecodeHanlder extends AbstractChannelHandlerDelegate {

    public DecodeHanlder(ChannelHandler channelHandler) {
        super(channelHandler);
    }
}
