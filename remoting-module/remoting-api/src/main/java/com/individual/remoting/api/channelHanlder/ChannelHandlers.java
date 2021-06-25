package com.individual.remoting.api.channelHanlder;

import com.individual.common.model.URL;

public class ChannelHandlers {

    public static ChannelHandler wrap(ChannelHandler channelHandler, URL url) {
        return channelHandler;
        // return ExtensionLoader.getExtensionLoader(Dispatcher.class).getAdaptiveExtension().dispatch(channelHandler, url);
    }
}
