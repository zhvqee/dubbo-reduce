package com.individual.serialization.api;

import java.io.IOException;

public interface ObjectOutput {


    /**
     * Write byte.
     *
     * @param v value.
     * @throws IOException
     */
    void writeByte(byte v) throws IOException;

    /**
     * Write short.
     *
     * @param v value.
     * @throws IOException
     */
    void writeShort(short v) throws IOException;

    /**
     * Write integer.
     *
     * @param v value.
     * @throws IOException
     */
    void writeInt(int v) throws IOException;

    /**
     * Write long.
     *
     * @param v value.
     * @throws IOException
     */
    void writeLong(long v) throws IOException;


    /**
     * Write string.
     *
     * @param v value.
     * @throws IOException
     */
    void writeUTF(String v) throws IOException;

    /**
     * Write byte array.
     *
     * @param v value.
     * @throws IOException
     */
    void writeBytes(byte[] v) throws IOException;

    /**
     * write object
     *
     * @param object
     * @throws IOException
     */
    void writeObject(Object object) throws IOException;

    /**
     * Flush buffer.
     *
     * @throws IOException
     */
    void flushBuffer() throws IOException;
}
