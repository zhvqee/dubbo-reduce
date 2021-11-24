package org.qee.cloud.rpc.protocol.impls;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.MethodReturnUtils;
import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.exchange.ExchangeClient;
import org.qee.cloud.remoting.api.exchange.ExchangeHandler;
import org.qee.cloud.remoting.api.exchange.ExchangeHandlerAdapter;
import org.qee.cloud.remoting.api.exchange.Exchangers;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.rpc.AsyncToSyncInvoker;
import org.qee.cloud.rpc.InvocationHandler;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.InvokerInvocationHandler;
import org.qee.cloud.rpc.Result;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.protocol.export.Exporter;
import org.qee.cloud.rpc.proxy.AsyncRpcResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudProtocol implements Protocol {

    private ExchangeHandler exchangeHandler = new ExchangeHandlerAdapter() {
        @Override
        public CompletableFuture<Object> reply(Channel channel, Object msg) throws RemotingException {
            if(msg instanceof  Request){//服务端收到请求
                Request request= (Request) msg;
                if(request.getData() instanceof InvokerInvocationHandler){
                    InvokerInvocationHandler invocationHandler= (InvokerInvocationHandler) request.getData();
                }


            }
            return null;
        }

        @Override
        public void sent(Channel channel, Object message) throws RemotingException {

        }

        @Override
        public void received(Channel channel, Object message) throws RemotingException {
            if (message instanceof Request) {
                System.out.println(message);
            } else if (message instanceof Response) {
                System.out.println(message);
            }
            reply(channel, message);
        }
    };

    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL providerUrl) {
        return new AsyncToSyncInvoker<>(getInvoker(refInterfaceClass, getExchangeClients(providerUrl), providerUrl));
    }


    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }

    private <T> Invoker<T> getInvoker(Class<T> refInterfaceClass, ExchangeClient[] exchangeClients, URL providerUrl) {
        return new Invoker<T>() {
            private AtomicInteger integer = new AtomicInteger();

            @Override
            public Class<T> getInterface() {
                return refInterfaceClass;
            }

            @Override
            public Result invoke(InvocationHandler invocationHandler) {
                ExchangeClient exchangeClient = null;
                if (exchangeClients.length == 1) {
                    exchangeClient = exchangeClients[0];
                } else {
                    exchangeClient = exchangeClients[integer.getAndIncrement() % exchangeClients.length];
                }
                Request request = new Request();
                request.setVersion(providerUrl.getParameter("version", "1.0"));
                request.setTwoWay(!MethodReturnUtils.isVoidClass(invocationHandler.getReturnType()));
                request.setData(invocationHandler);
                CompletableFuture<Response> completableFuture = exchangeClient.request(request);
                return new AsyncRpcResult(completableFuture, invocationHandler);
            }
        };
    }

    private ExchangeClient[] getExchangeClients(URL url) {
        Integer iothreads = Integer.parseInt(url.getParameter("connections"), 1);
        ExchangeClient[] exchangeClient = new ExchangeClient[iothreads];
        for (int i = 0; i < iothreads; i++) {
            exchangeClient[i] = Exchangers.connect(url, exchangeHandler);
        }
        return exchangeClient;

    }


}
