package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.channelHanlder.AbstractChannelHandlerDelegate;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;

public class HeaderExchangeHandler extends AbstractChannelHandlerDelegate {
    public HeaderExchangeHandler(ChannelHandler channelHandler) {
        super(channelHandler);
    }
}
