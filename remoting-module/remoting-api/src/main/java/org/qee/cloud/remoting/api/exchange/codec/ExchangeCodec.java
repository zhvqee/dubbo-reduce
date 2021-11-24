package org.qee.cloud.remoting.api.exchange.codec;

import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.utils.ByteUtils;
import org.qee.cloud.remoting.api.buffer.ChannelBuffer;
import org.qee.cloud.remoting.api.buffer.stream.ChannelBufferInputStream;
import org.qee.cloud.remoting.api.buffer.stream.ChannelBufferOutputStream;
import org.qee.cloud.remoting.api.codec.Codec;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.serialization.api.ObjectInput;
import org.qee.cloud.serialization.api.ObjectOutput;
import org.qee.cloud.serialization.api.Serialization;

import java.io.IOException;

/**
 * 交换层 编解码
 * <p> 2+1+1+8+4 =16字节 1
 * +----------------------------------------------------------------+|
 * | 2 magic | 1 flag |1 status | 8 invokerId | 4 body length | body |
 * |  0,1    | 2      | 3       | 4,5,6,7,8,9,10,11 |  12          |   n  |
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


    // header length.
    protected static final int HEADER_LENGTH = 16;


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

    /**
     * buffer decode return object
     * <p>
     * 上面编码的时候根据request 和response 分别进行编码，
     * 但是在解码的时候则不需要，处理16字节的头部外，剩余是可以组成一个返回对象
     * 接着根据 flag 来判断是请求还是响应，然后转（要么request或者response）
     * 通过反序列化方法objectInput.readObject() 就可以得到一个对象。不需要关注类型
     *
     * @param buffer
     * @return
     * @throws IOException
     */
    //这里更改buffer 已足够解析，前面用了 LengthFieldBasedFrameDecoder

    /**
     * 交换层 编解码
     * <p> 2+1+1+8+4 =16字节 1
     * +----------------------------------------------------------------+|
     * | 2 magic | 1 flag |1 status | 8 invokerId       | 4 body length | body |
     * |  0,1    | 2      | 3       | 4,5,6,7,8,9,10,11 |  12          |   n  |
     * +-----------------------------------------------------------------+
     */

    @Override
    public Object decode(ChannelBuffer buffer) throws IOException, ClassNotFoundException {

        byte[] header = new byte[HEADER_LENGTH];
        buffer.readBytes(header);
        if ((header[2] & FLAG_REQUEST) != 0) {
            long id = ByteUtils.byte2Long(header, 4);
            Request request = new Request();
            request.setId(id);
            if ((header[2] & FLAG_TWOWAY) != 0) {
                request.setTwoWay(true);
            }
            request.setVersion("1.0"); //先写死
            Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtension();
            ChannelBufferInputStream inputStream = new ChannelBufferInputStream(buffer);
            ObjectInput objectInput = serialization.deserilize(inputStream);
            request.setData(objectInput.readObject());
            return request;
        } else {
            long id = ByteUtils.byte2Long(header, 4);
            Response response = new Response();
            response.setId(id);
            response.setStatus(header[3]);

            Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtension();
            ChannelBufferInputStream inputStream = new ChannelBufferInputStream(buffer);
            ObjectInput objectInput = serialization.deserilize(inputStream);
            response.setData(objectInput.readObject());
            return response;
        }
    }


    private void encodeRequest(ChannelBuffer buffer, Request request) throws IOException {
        byte[] header = new byte[HEADER_LENGTH];

        ByteUtils.writeShort(header, 0, MAGIC);

        header[2] = FLAG_REQUEST;
        if (request.isTwoWay()) {
            header[2] |= FLAG_TWOWAY;
        }
        ByteUtils.writeLong(header, 4, request.getId());

        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtension();

        // encode request data.
        int savedWriteIndex = buffer.writerIndex();
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
        ChannelBufferOutputStream outputStream = new ChannelBufferOutputStream(buffer);
        ObjectOutput objectOutput = serialization.serialize(outputStream);
        objectOutput.writeObject(request.getData());
        objectOutput.flushBuffer();
        int writtenBytes = outputStream.getWrittenBytes();
        outputStream.flush();
        outputStream.close();
        checkPayload(writtenBytes);

        ByteUtils.writeInt(header, 12, writtenBytes);

        buffer.writerIndex(savedWriteIndex);
        outputStream.write(header);
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + writtenBytes);
    }

    private void checkPayload(int writtenBytes) {
    }

    //2 magic | 1 flag |1 status | 8 invokerId | 4 body length | body |
    private void encodeResponse(ChannelBuffer buffer, Response response) throws IOException {
        byte[] header = new byte[HEADER_LENGTH];

        ByteUtils.writeShort(header, 0, MAGIC);
        header[3] = (byte) response.getStatus();
        ByteUtils.writeLong(header, 4, response.getId());

        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtension();

        int readerIndex = buffer.readerIndex();
        buffer.readerIndex(readerIndex + HEADER_LENGTH);
        ChannelBufferOutputStream outputStream = new ChannelBufferOutputStream(buffer);
        ObjectOutput objectOutput = serialization.serialize(outputStream);
        objectOutput.writeObject(response.getData());
        int writtenBytes = outputStream.getWrittenBytes();
        outputStream.flush();
        outputStream.close();
        ByteUtils.writeInt(header, 12, writtenBytes);

        buffer.writerIndex(readerIndex);
        outputStream.write(header);
        buffer.writerIndex(readerIndex + HEADER_LENGTH + writtenBytes);
    }


}
