package com.individual.remoting.api.exchange.codec;

import com.individual.remoting.api.buffer.ChannelBuffer;
import com.individual.remoting.api.codec.Codec;
import com.individual.remoting.api.exchange.request.Request;
import com.individual.remoting.api.exchange.response.Response;

import java.io.IOException;

/**
 * 交换层 编解码
 * <p> 2+1+1+8+4 =16字节 1
 * +----------------------------------------------------------------+|
 * | 2 magic | 1 flag |1 status | 8 invokerId | 4 body length | body |
 * | 2       | seril  | 1       |  8          |  4            |   n  |
 * +-----------------------------------------------------------------+
 */


public class ExchangeCodec implements Codec {

    /**
     * message encode to buffer
     *
     * @param buffer
     * @param message
     * @throws IOException
     */
    @Override
    public void encode(ChannelBuffer buffer, Object message) throws IOException {
        if (message instanceof Request) {
            Request request = (Request) message;
            encodeRequest(buffer, request);

        } else if (message instanceof Response) {
            Response response = (Response) message;
            encodeResponse(buffer, response);
        }
    }

    private void encodeResponse(ChannelBuffer buffer, Response response) {
    }

    private void encodeRequest(ChannelBuffer buffer, Request request) {

    }


    /**
     * buffer decode return object
     *
     * @param buffer
     * @return
     * @throws IOException
     */

    @Override
    public Object decode(ChannelBuffer buffer) throws IOException {
        return null;
    }
}
