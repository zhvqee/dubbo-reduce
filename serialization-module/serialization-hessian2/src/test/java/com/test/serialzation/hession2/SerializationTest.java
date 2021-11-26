package com.test.serialzation.hession2;


import org.qee.cloud.serialization.api.ObjectInput;
import org.qee.cloud.serialization.api.ObjectOutput;
import org.qee.cloud.serialization.concretion.hessian2.Hession2Serialization;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SerializationTest {

    @Test
    public void testHession2Serialization() throws IOException, ClassNotFoundException {

        Hession2Serialization hession2Serialization = new Hession2Serialization();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = hession2Serialization.serialize(outputStream);

        objectOutput.writeByte((byte) 1);
        objectOutput.writeInt(1024);
        objectOutput.writeLong(9527520);
        objectOutput.writeShort((short) 127);
        objectOutput.writeUTF("what to do ?");
        objectOutput.writeBytes("hello".getBytes());

        User user = new User();
        user.setAge(13);
        user.setName("Jame");
        user.setSex(1);
        objectOutput.writeObject(user);
        objectOutput.writeBytes(new byte[]{'a', 'b'});
        objectOutput.flushBuffer();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = hession2Serialization.deserilize(inputStream);

        System.out.println(objectInput.readByte());
        System.out.println(objectInput.readInt());
        System.out.println(objectInput.readLong());
        System.out.println(objectInput.readShort());
        System.out.println(objectInput.readUTF());
        System.out.println(objectInput.readBytes());
        System.out.println(objectInput.readObject(User.class));
    }


}
