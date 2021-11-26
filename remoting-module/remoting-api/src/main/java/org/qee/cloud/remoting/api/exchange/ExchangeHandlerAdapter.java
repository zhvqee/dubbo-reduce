package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.remoting.api.channel.Channel;

import java.util.concurrent.CompletableFuture;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.remoting.api.exchange
 * @ClassName: ExchangeHandlerAdapter
 * @Description:
 * @Date: 2021/11/24 2:19 下午
 * @Version: 1.0
 */
public class ExchangeHandlerAdapter implements ExchangeHandler {
    @Override
    public CompletableFuture<Object> reply(Channel channel, Object request) throws RemotingException {
        return null;
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        System.out.println("客户端链接" + channel.toString());

    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        System.out.println("客户端断开链接" + channel.toString());
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {
        System.out.println("发送数据" + channel.toString());
    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {
        System.out.println("接收数据" + channel.toString());
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        System.out.println("异常：" + exception);
    }
}
