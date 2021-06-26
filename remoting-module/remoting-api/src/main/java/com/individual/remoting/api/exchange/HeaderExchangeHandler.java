package com.individual.remoting.api.exchange;

import com.individual.remoting.api.channelHanlder.AbstractChannelHandlerDelegate;
import com.individual.remoting.api.channelHanlder.ChannelHandler;

public class HeaderExchangeHandler extends AbstractChannelHandlerDelegate {
    public HeaderExchangeHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }
}
