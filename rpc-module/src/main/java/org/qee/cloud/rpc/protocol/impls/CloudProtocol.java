package org.qee.cloud.rpc.protocol.impls;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.exchange.ExchangeClient;
import org.qee.cloud.remoting.api.exchange.ExchangeHandler;
import org.qee.cloud.remoting.api.exchange.Exchangers;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.rpc.AsyncToSyncInvoker;
import org.qee.cloud.rpc.InvocationHandler;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.Result;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.protocol.export.Exporter;
import org.qee.cloud.rpc.proxy.AsyncRpcResult;

import java.util.concurrent.atomic.AtomicInteger;

public class CloudProtocol implements Protocol {

    private ExchangeHandler exchangeHandler = null;

    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL providerUrl) {
        return new AsyncToSyncInvoker<>(getInvoker(refInterfaceClass, getExchangeClients(providerUrl)));
    }


    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }

    private <T> Invoker<T> getInvoker(Class<T> refInterfaceClass, ExchangeClient[] exchangeClients) {
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
                Response response = exchangeClient.request(request);
                AsyncRpcResult asyncRpcResult= new AsyncRpcResult(,invocationHandler);
                return asyncRpcResult;
            }
        };
    }

    private ExchangeClient[] getExchangeClients(URL url) {
        Integer accepts = Integer.parseInt(url.getParameter("accepts"), 1);
        ExchangeClient[] exchangeClient = new ExchangeClient[accepts];
        for (int i = 0; i < accepts; i++) {
            exchangeClient[i] = Exchangers.connect(url, exchangeHandler);
        }
        return exchangeClient;

    }


}
