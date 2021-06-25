package com.individual.remoting.netty4.codec;

import com.individual.remoting.api.buffer.ChannelBuffer;
import com.individual.remoting.api.codec.Codec;
import com.individual.remoting.netty4.buffer.NettyBackedChannelBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.List;

public class NettyCodecAdapter {

    private ChannelHandler encoder = new InternalEncoder();

    private ChannelHandler decoder = new InternalDecoder();


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

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            ChannelBuffer channelBuffer = new NettyBackedChannelBuffer(in);
            int readerIndex = channelBuffer.readerIndex();

            while (true) {
                Object result = NettyCodecAdapter.this.codec.decode(channelBuffer);
                if (result == Codec.DecodeResult.NEED_MORE_INPUT) {
                    channelBuffer.readerIndex(readerIndex);
                    break;
                } else {
                    if (readerIndex == channelBuffer.readerIndex()) {
                        throw new IOException("Decode without read data.");
                    }
                }
                if (result != null) {
                    out.add(result);
                }
            }
        }
    }
}
