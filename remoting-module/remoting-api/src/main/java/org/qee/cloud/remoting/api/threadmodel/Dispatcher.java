package org.qee.cloud.remoting.api.threadmodel;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;

public interface Dispatcher {

    ChannelHandler dispatch(ChannelHandler handler, URL url);
}
