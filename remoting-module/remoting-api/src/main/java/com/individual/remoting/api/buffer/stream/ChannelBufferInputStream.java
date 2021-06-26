package com.individual.remoting.api.buffer.stream;

import com.individual.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.InputStream;

public class ChannelBufferInputStream extends InputStream {

    private ChannelBuffer channelBuffer;

    public ChannelBufferInputStream(ChannelBuffer channelBuffer) {
        this.channelBuffer = channelBuffer;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}
