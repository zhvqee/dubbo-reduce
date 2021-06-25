package com.individual.common.exceptions;

import java.net.InetSocketAddress;

public class RemotingException extends Exception {


    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;


    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }


    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(message, cause);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
