package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.transport.server.RemotingServer;

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
