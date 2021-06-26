package com.individual.remoting.api.exchange;

import com.individual.common.exceptions.RemotingException;
import com.individual.common.model.URL;

/**
 * 交换层 抽象，它和传输层类似，主要是对交换这件事做处理，所以会封装channelHandler .
 * 在交换层中，我们已不和底层的channel 进行打交道了，
 * 我们直接面对的是request,response 类别的对象，
 */
public interface Exchanger {


    /**
     * 绑定创建一个服务端，这里默认采用nettyServer
     *
     * @return
     */
    ExchangeServer bind(URL url, ExchangeHandler channelHandler) throws RemotingException;


    ExchangeClient connect(URL url, ExchangeHandler channelHandler) throws RemotingException;
}
