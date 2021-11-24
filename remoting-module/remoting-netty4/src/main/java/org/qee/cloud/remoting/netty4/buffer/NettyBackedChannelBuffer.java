package org.qee.cloud.remoting.netty4.buffer;

import io.netty.buffer.ByteBuf;
import org.qee.cloud.remoting.api.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class NettyBackedChannelBuffer implements ChannelBuffer {

    private ByteBuf byteBuf;

    public NettyBackedChannelBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public void clear() {
        byteBuf.clear();
    }

    @Override
    public void getBytes(int index, ByteBuffer dst) {
        byteBuf.getBytes(index, dst);

    }

    @Override
    public void getBytes(int index, ChannelBuffer dst) {
        getBytes(index, dst, dst.writableBytes());
    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int length) {
        // careful
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
        // careful
        byte[] data = new byte[length];
        byteBuf.getBytes(index, data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }

    @Override
    public void getBytes(int index, OutputStream dst, int length) throws IOException {
        byteBuf.getBytes(index, dst, length);
    }

    @Override
    public boolean isDirect() {
        return byteBuf.isDirect();
    }

    @Override
    public void markReaderIndex() {
        byteBuf.markReaderIndex();
    }

    @Override
    public void markWriterIndex() {
        byteBuf.markReaderIndex();
    }

    @Override
    public boolean readable() {
        return byteBuf.isReadable();
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public void readBytes(byte[] dst) {
        byteBuf.readBytes(dst);
    }

    @Override
    public void readBytes(byte[] dst, int dstIndex, int length) {
        byteBuf.readBytes(dst, dstIndex, length);
    }

    @Override
    public void readBytes(ByteBuffer dst) {
        byteBuf.readBytes(dst);
    }

    @Override
    public void readBytes(ChannelBuffer dst) {
        readBytes(dst, dst.writableBytes());
    }

    @Override
    public void readBytes(ChannelBuffer dst, int length) {
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }

    @Override
    public void readBytes(ChannelBuffer dst, int dstIndex, int length) {
        if (readableBytes() < length) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = new byte[length];
        byteBuf.readBytes(data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }

    @Override
    public ChannelBuffer readBytes(int length) {
        return new NettyBackedChannelBuffer(byteBuf.readBytes(length));
    }

    @Override
    public void resetReaderIndex() {
        byteBuf.resetReaderIndex();
    }

    @Override
    public void resetWriterIndex() {
        byteBuf.resetWriterIndex();
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public void readerIndex(int readerIndex) {
        byteBuf.readerIndex(readerIndex);
    }

    @Override
    public void readBytes(OutputStream dst, int length) throws IOException {
        byteBuf.readBytes(dst, length);
    }

    @Override
    public void setByte(int index, int value) {
        byteBuf.setByte(index, value);
    }

    @Override
    public void setBytes(int index, byte[] src) {
        byteBuf.setBytes(index, src);
    }

    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        byteBuf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public void setBytes(int index, ByteBuffer src) {
        byteBuf.setBytes(index, src);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src) {
        // careful
        setBytes(index, src, src.readableBytes());
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int length) {
        // careful
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        byte[] data = new byte[length];
        byteBuf.getBytes(srcIndex, data, 0, length);
        setBytes(index, data, 0, length);
    }

    @Override
    public int setBytes(int index, InputStream src, int length) throws IOException {
        return byteBuf.setBytes(index, src, length);
    }

    @Override
    public void setIndex(int readerIndex, int writerIndex) {
        byteBuf.setIndex(readerIndex, writerIndex);
    }

    @Override
    public void skipBytes(int length) {
        byteBuf.skipBytes(length);
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return byteBuf.nioBuffer();
    }

    @Override
    public ByteBuffer toByteBuffer(int index, int length) {
        return byteBuf.nioBuffer(index, length);
    }

    @Override
    public boolean writable() {
        return byteBuf.isWritable();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public void writeByte(int value) {
        byteBuf.writeByte(value);
    }

    @Override
    public void writeBytes(byte[] src) {
        byteBuf.writeBytes(src);
    }

    @Override
    public void writeBytes(byte[] src, int index, int length) {
        byteBuf.writeBytes(src, index, length);
    }

    @Override
    public void writeBytes(ByteBuffer src) {
        byteBuf.writeBytes(src);
    }

    @Override
    public void writeBytes(ChannelBuffer src) {
        writeBytes(src, src.writableBytes());
    }

    @Override
    public void writeBytes(ChannelBuffer src, int length) {
        writeBytes(src, 0, length);
    }

    @Override
    public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
        byte[] data = new byte[length];
        src.readBytes(data, 0, length);
        byteBuf.writeBytes(data, 0, length);
    }

    @Override
    public int writeBytes(InputStream src, int length) throws IOException {
        return byteBuf.writeBytes(src, length);
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public void writerIndex(int writerIndex) {
        byteBuf.writerIndex(writerIndex);
    }

    @Override
    public byte[] array() {
        return byteBuf.array();
    }

    @Override
    public boolean hasArray() {
        return byteBuf.hasArray();
    }

    @Override
    public int arrayOffset() {
        return byteBuf.arrayOffset();
    }
}
