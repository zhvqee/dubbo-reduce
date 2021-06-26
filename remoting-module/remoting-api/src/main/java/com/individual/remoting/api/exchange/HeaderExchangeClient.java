package com.individual.remoting.api.exchange;

import com.individual.remoting.api.transport.client.Client;

public class HeaderExchangeClient implements ExchangeClient {

    private Client client;

    public HeaderExchangeClient(Client client) {
        this.client = client;
    }
}
