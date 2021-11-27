package org.qee.cloud.remoting.api.buffer.stream;

import org.qee.cloud.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.InputStream;

public class ChannelBufferInputStream extends InputStream {

    private ChannelBuffer channelBuffer;

    public ChannelBufferInputStream(ChannelBuffer channelBuffer) {
        this.channelBuffer = channelBuffer;
    }

    @Override
    public int read() throws IOException {
        if (!channelBuffer.readable()) {
            return -1;
        }
        return channelBuffer.readByte() & 0xff;
    }
}
