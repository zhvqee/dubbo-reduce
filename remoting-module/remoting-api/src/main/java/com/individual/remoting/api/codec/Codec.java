package com.individual.remoting.api.codec;

import com.individual.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * 编解码
 */
public interface Codec {

    /**
     * 把 消息 编码到buffer
     *
     * @param buffer
     * @param message
     * @throws IOException
     */
    void encode(ChannelBuffer buffer, Object message) throws IOException;

    /**
     * 把 buffer 内容解码为object
     *
     * @param buffer
     * @return
     * @throws IOException
     */
    Object decode(ChannelBuffer buffer) throws IOException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
