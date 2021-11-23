package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.remoting.api.transport.client.Client;

public class HeaderExchangeClient implements ExchangeClient {

    private Client client;

    public HeaderExchangeClient(Client client) {
        this.client = client;
    }

    @Override
    public Response request(Request request) {
        return null;
    }
}
