package org.qee.cloud.remoting.api.buffer.stream;

import org.qee.cloud.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.OutputStream;

public class ChannelBufferOutputStream extends OutputStream {

    private ChannelBuffer buffer;

    private final int startWriteIndex;

    public ChannelBufferOutputStream(ChannelBuffer buffer) {
        this.buffer = buffer;
        this.startWriteIndex = buffer.writerIndex();
    }

    public int getWrittenBytes() {
        return buffer.writerIndex() - startWriteIndex;
    }

    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }
}
