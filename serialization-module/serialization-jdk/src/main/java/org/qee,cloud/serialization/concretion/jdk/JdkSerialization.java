package com.individual.serialization.concretion.jdk;

import org.qee.cloud.serialization.api.ObjectInput;
import org.qee.cloud.serialization.api.ObjectOutput;
import org.qee.cloud.serialization.api.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ProjectName: dubbo-reduce
 * @Package: com.individual.serialization.concretion.jdk
 * @ClassName: JdkSerialization
 * @Description:
 * @Date: 2021/11/16 7:47 下午
 * @Version: 1.0
 */
public class JdkSerialization implements Serialization {
    @Override
    public ObjectOutput serialize(OutputStream os) throws IOException {
        return new JdkObjectOutput(os);
    }

    @Override
    public ObjectInput deserilize(InputStream is) throws IOException {
        return new JdkObjectInput(is);
    }
}
