package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.transport.client.Client;

public class HeaderExchangeClient implements ExchangeClient {

    private Client client;

    public HeaderExchangeClient(Client client) {
        this.client = client;
    }
}
