package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;

/**
 * 交换层 抽象，它和传输层类似，主要是对交换这件事做处理，所以会封装channelHandler .
 * 在交换层中，我们已不和底层的channel 进行打交道了，
 * 我们直接面对的是request,response 类别的对象，
 */
@SPI(name = "header")
public interface Exchanger {

    /**
     * 绑定创建一个服务端，这里默认采用nettyServer
     *
     * @return
     */
    @Adaptive(keys = "exchanger")
    ExchangeServer bind(URL url, ExchangeHandler channelHandler) throws RemotingException;

    @Adaptive(keys = "exchanger")
    ExchangeClient connect(URL url, ExchangeHandler channelHandler) throws RemotingException;
}
