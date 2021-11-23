package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.remoting.api.transport.client.Client;

import java.util.concurrent.CompletableFuture;

public class HeaderExchangeClient implements ExchangeClient {

    private Client client;

    public HeaderExchangeClient(Client client) {
        this.client = client;

        //dubbo 在这里是为了心跳检测和重连
        //这里我们待做
        // TODO: 2021/11/23

    }

    @Override
    public CompletableFuture<Response> request(Request request) {
        return client.request(request);
    }
}
