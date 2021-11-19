package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.remoting.api.channelHanlder.ChannelHandler;

import java.util.concurrent.CompletableFuture;

public interface ExchangeHandler extends ChannelHandler {

    /**
     * 对于交换层，我们有种模式，叫做 ping-pong ,既有应答的方式
     *
     * @param channel
     * @param request
     * @return
     * @throws RemotingException
     */
    CompletableFuture<Object> reply(ExchangeChannel channel, Object request) throws RemotingException;

}
