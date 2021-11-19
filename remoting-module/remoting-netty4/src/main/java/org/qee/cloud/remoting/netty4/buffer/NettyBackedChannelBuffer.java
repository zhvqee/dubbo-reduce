package org.qee.cloud.remoting.netty4.buffer;

import org.qee.cloud.remoting.api.buffer.ChannelBuffer;
import io.netty.buffer.ByteBuf;

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

    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int length) {

    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {

    }

    @Override
    public void getBytes(int index, OutputStream dst, int length) throws IOException {

    }

    @Override
    public boolean isDirect() {
        return false;
    }

    @Override
    public void markReaderIndex() {

    }

    @Override
    public void markWriterIndex() {

    }

    @Override
    public boolean readable() {
        return false;
    }

    @Override
    public int readableBytes() {
        return 0;
    }

    @Override
    public byte readByte() {
        return 0;
    }

    @Override
    public void readBytes(byte[] dst) {

    }

    @Override
    public void readBytes(byte[] dst, int dstIndex, int length) {

    }

    @Override
    public void readBytes(ByteBuffer dst) {

    }

    @Override
    public void readBytes(ChannelBuffer dst) {

    }

    @Override
    public void readBytes(ChannelBuffer dst, int length) {

    }

    @Override
    public void readBytes(ChannelBuffer dst, int dstIndex, int length) {

    }

    @Override
    public ChannelBuffer readBytes(int length) {
        return null;
    }

    @Override
    public void resetReaderIndex() {

    }

    @Override
    public void resetWriterIndex() {

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

    }

    @Override
    public void setByte(int index, int value) {

    }

    @Override
    public void setBytes(int index, byte[] src) {

    }

    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {

    }

    @Override
    public void setBytes(int index, ByteBuffer src) {

    }

    @Override
    public void setBytes(int index, ChannelBuffer src) {

    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int length) {

    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {

    }

    @Override
    public int setBytes(int index, InputStream src, int length) throws IOException {
        return 0;
    }

    @Override
    public void setIndex(int readerIndex, int writerIndex) {

    }

    @Override
    public void skipBytes(int length) {

    }

    @Override
    public ByteBuffer toByteBuffer() {
        return null;
    }

    @Override
    public ByteBuffer toByteBuffer(int index, int length) {
        return null;
    }

    @Override
    public boolean writable() {
        return false;
    }

    @Override
    public int writableBytes() {
        return 0;
    }

    @Override
    public void writeByte(int value) {

    }

    @Override
    public void writeBytes(byte[] src) {

    }

    @Override
    public void writeBytes(byte[] src, int index, int length) {

    }

    @Override
    public void writeBytes(ByteBuffer src) {

    }

    @Override
    public void writeBytes(ChannelBuffer src) {

    }

    @Override
    public void writeBytes(ChannelBuffer src, int length) {

    }

    @Override
    public void writeBytes(ChannelBuffer src, int srcIndex, int length) {

    }

    @Override
    public int writeBytes(InputStream src, int length) throws IOException {
        return 0;
    }

    @Override
    public int writerIndex() {
        return 0;
    }

    @Override
    public void writerIndex(int writerIndex) {

    }

    @Override
    public byte[] array() {
        return new byte[0];
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public int arrayOffset() {
        return 0;
    }
}
