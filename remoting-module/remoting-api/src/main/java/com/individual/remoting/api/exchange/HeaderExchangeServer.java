package com.individual.remoting.api.exchange;

import com.individual.remoting.api.channel.Channel;
import com.individual.remoting.api.transport.server.RemotingServer;

import java.util.Collection;

public class HeaderExchangeServer implements ExchangeServer {

    private RemotingServer server;

    public HeaderExchangeServer(RemotingServer server) {
        this.server = server;
    }

    @Override
    public Collection<Channel> getAlreadyClientChannels() {
        return null;
    }
}
