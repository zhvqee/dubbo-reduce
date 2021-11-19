package org.qee.cloud.remoting.api.channelHanlder;

import org.qee.cloud.common.model.URL;

public class ChannelHandlers {

    public static ChannelHandler wrap(ChannelHandler channelHandler, URL url) {
        return channelHandler;
        // return ExtensionLoader.getExtensionLoader(Dispatcher.class).getAdaptiveExtension().dispatch(channelHandler, url);
    }
}
