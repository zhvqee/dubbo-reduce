package com.individual.remoting.api.exchange.codec;

import com.individual.common.extentions.ExtensionLoader;
import com.individual.common.utils.ByteUtils;
import com.individual.remoting.api.buffer.ChannelBuffer;
import com.individual.remoting.api.codec.Codec;
import com.individual.remoting.api.exchange.request.Request;
import com.individual.remoting.api.exchange.response.Response;
import com.individual.serialization.api.Serialization;
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
     * 魔数
     */
    protected static final short MAGIC = (short) 0xaabb;

    protected static final byte FLAG_REQUEST = (byte) 0x80;
    protected static final byte FLAG_TWOWAY = (byte) 0x40;
    protected static final byte FLAG_EVENT = (byte) 0x20;


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

    private void encodeRequest(ChannelBuffer buffer, Request request) {
        byte[] header = new byte[16];

        ByteUtils.writeShort(header, 0, MAGIC);

        header[2] = FLAG_REQUEST;
        if (request.isTwoWay()) {
            header[2] |= FLAG_TWOWAY;
        }
        ByteUtils.writeLong(header, 3, request.getId());

        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtension();
        serialization.


    }

    private void encodeResponse(ChannelBuffer buffer, Response response) {


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
