package org.qee.cloud.serialization.concretion.hessian2;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import org.qee.cloud.serialization.api.ObjectInput;

import java.io.IOException;
import java.io.InputStream;

public class Hession2ObjectInput implements ObjectInput {

    private Hessian2Input hessian2Input;

    public Hession2ObjectInput(InputStream inputStream) {
        this.hessian2Input = new Hessian2Input(inputStream);
    }

    public byte readByte() throws IOException {
        return (byte) hessian2Input.readInt();
    }

    public short readShort() throws IOException {
        return hessian2Input.readShort();
    }

    public int readInt() throws IOException {
        return hessian2Input.readInt();
    }

    public long readLong() throws IOException {
        return hessian2Input.readLong();
    }

    public String readUTF() throws IOException {
        return hessian2Input.readString();
    }

    public byte[] readBytes() throws IOException {
        return hessian2Input.readBytes();
    }

    public Object readObject() throws IOException {
        return hessian2Input.readObject();
    }

    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) hessian2Input.readObject(cls);
    }
}
