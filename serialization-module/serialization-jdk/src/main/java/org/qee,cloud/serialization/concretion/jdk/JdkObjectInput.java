package com.individual.serialization.concretion.jdk;

import org.qee.cloud.serialization.api.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @ProjectName: qee-cloud
 * @Package: com.individual.serialization.concretion.jdk
 * @ClassName: JdkObjectInput
 * @Description:
 * @Date: 2021/11/16 7:49 下午
 * @Version: 1.0
 */
public class JdkObjectInput implements ObjectInput {

    private ObjectInputStream objectInputStream;

    public JdkObjectInput(InputStream inputStream) throws IOException {
        this.objectInputStream = new ObjectInputStream(inputStream);
    }

    @Override
    public byte readByte() throws IOException {
        return objectInputStream.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return objectInputStream.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return objectInputStream.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return objectInputStream.readLong();
    }

    @Override
    public String readUTF() throws IOException {
        return objectInputStream.readUTF();
    }

    @Override
    public byte[] readBytes() throws IOException {
        int length = objectInputStream.readInt();
        if (length == 0) {
            return new byte[0];
        }
        byte[] bytes = new byte[length];
        objectInputStream.readFully(bytes, 0, bytes.length);
        return bytes;
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }
}
