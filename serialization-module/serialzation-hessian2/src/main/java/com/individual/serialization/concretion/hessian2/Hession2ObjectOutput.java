package com.individual.serialization.concretion.hessian2;

import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.individual.serialization.api.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

public class Hession2ObjectOutput implements ObjectOutput {

    private Hessian2Output hessian2Output;

    public Hession2ObjectOutput(OutputStream outputStream) {
        hessian2Output = new Hessian2Output(outputStream);
    }

    public void writeByte(byte v) throws IOException {
        hessian2Output.writeInt(v);
    }

    public void writeShort(short v) throws IOException {
        hessian2Output.writeInt(v);
    }

    public void writeInt(int v) throws IOException {
        hessian2Output.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        hessian2Output.writeLong(v);
    }

    public void writeUTF(String v) throws IOException {
        hessian2Output.writeString(v);
    }

    public void writeBytes(byte[] v) throws IOException {
        hessian2Output.writeBytes(v);
    }

    public void writeObject(Object object) throws IOException {
        hessian2Output.writeObject(object);
    }

    public void flushBuffer() throws IOException {
        hessian2Output.flushBuffer();
    }
}
