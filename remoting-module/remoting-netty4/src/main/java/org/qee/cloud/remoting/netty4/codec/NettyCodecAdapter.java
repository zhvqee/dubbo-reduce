package org.qee.cloud.remoting.netty4.codec;

import org.qee.cloud.remoting.api.codec.Codec;
import org.qee.cloud.remoting.netty4.buffer.NettyBackedChannelBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;

public class NettyCodecAdapter {

    private ChannelHandler encoder = new InternalEncoder();

    private ChannelHandler decoder = new InternalDecoder();

    private static final int MAX_LENGTH_FRAME = 3 * 1024 * 1204;


    private Codec codec;

    public NettyCodecAdapter(Codec codec) {
        this.codec = codec;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }


    public ChannelHandler getDecoder() {
        return decoder;
    }

    private class InternalEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            NettyCodecAdapter.this.codec.encode(new NettyBackedChannelBuffer(out), msg);
        }
    }

    /**
     * 交换层 编解码
     * <p> 2+1+1+8+4 =16字节 1
     * +----------------------------------------------------------------+|
     * | 2 magic | 1 flag |1 status | 8 invokerId       | 4 body length | body |
     * |  0,1    | 2      | 3       | 4,5,6,7,8,9,10,11 |  12          |   n  |
     * +-----------------------------------------------------------------+
     */

    private class InternalDecoder extends LengthFieldBasedFrameDecoder {
        public InternalDecoder() {
            this(ByteOrder.BIG_ENDIAN, MAX_LENGTH_FRAME, 12, 4, 0, 0, true);
        }

        public InternalDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
            super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            Object decode = super.decode(ctx, in);
            if (decode != null) {
                return NettyCodecAdapter.this.codec.decode(new NettyBackedChannelBuffer(in));
            }
            return null;
        }
    }
}
