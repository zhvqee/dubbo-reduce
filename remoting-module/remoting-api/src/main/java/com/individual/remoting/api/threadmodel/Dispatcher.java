package com.individual.remoting.api.threadmodel;

import com.individual.common.model.URL;
import com.individual.remoting.api.channelHanlder.ChannelHandler;

public interface Dispatcher {

    ChannelHandler dispatch(ChannelHandler handler, URL url);
}
