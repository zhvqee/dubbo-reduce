package org.qee.cloud.rpc.api.protocol.export;


import org.qee.cloud.remoting.api.exchange.ExchangeServer;
import org.qee.cloud.rpc.api.Invoker;

public class Exporter<T> {

    private Invoker<T> invoker;

    private ExchangeServer exchangeServer;

    public Exporter(Invoker<T> invoker, ExchangeServer exchangeServer) {
        this.invoker = invoker;
        this.exchangeServer = exchangeServer;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }
}
