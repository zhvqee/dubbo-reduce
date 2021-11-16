package com.individual.serialization.concretion.jdk;

import com.individual.serialization.api.ObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @ProjectName: dubbo-reduce
 * @Package: com.individual.serialization.concretion.jdk
 * @ClassName: JdkObjectOutput
 * @Description:
 * @Date: 2021/11/16 7:48 下午
 * @Version: 1.0
 */
public class JdkObjectOutput implements ObjectOutput {

    private ObjectOutputStream objectOutputStream;

    public JdkObjectOutput(OutputStream outputStream) throws IOException {
        this.objectOutputStream = new ObjectOutputStream(outputStream);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        objectOutputStream.write(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        objectOutputStream.writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        objectOutputStream.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        objectOutputStream.writeLong(v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        objectOutputStream.writeUTF(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        objectOutputStream.writeInt(v.length);
        objectOutputStream.write(v, 0, v.length);
    }

    @Override
    public void writeObject(Object object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    @Override
    public void flushBuffer() throws IOException {
        objectOutputStream.flush();
    }
}
