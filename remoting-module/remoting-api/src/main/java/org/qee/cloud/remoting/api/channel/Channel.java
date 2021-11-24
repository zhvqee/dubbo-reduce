package org.qee.cloud.remoting.api.channel;

public interface Channel {
    void close();

    void sent(Object msg);
}
