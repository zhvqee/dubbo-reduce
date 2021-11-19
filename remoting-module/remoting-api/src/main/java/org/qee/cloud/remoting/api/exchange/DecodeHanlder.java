package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.channelHanlder.AbstractChannelHandlerDelegate;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;

public class DecodeHanlder extends AbstractChannelHandlerDelegate {

    public DecodeHanlder(ChannelHandler channelHandler) {
        super(channelHandler);
    }
}
