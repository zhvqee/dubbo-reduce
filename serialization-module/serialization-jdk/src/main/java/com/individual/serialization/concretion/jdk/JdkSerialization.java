package com.individual.serialization.concretion.jdk;

import com.individual.serialization.api.ObjectInput;
import com.individual.serialization.api.ObjectOutput;
import com.individual.serialization.api.Serialization;

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
