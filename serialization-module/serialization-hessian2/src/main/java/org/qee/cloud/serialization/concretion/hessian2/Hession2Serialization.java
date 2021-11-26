package org.qee.cloud.serialization.concretion.hessian2;

import org.qee.cloud.serialization.api.ObjectInput;
import org.qee.cloud.serialization.api.ObjectOutput;
import org.qee.cloud.serialization.api.Serialization;

import java.io.InputStream;
import java.io.OutputStream;

public class Hession2Serialization implements Serialization {


    public ObjectOutput serialize(OutputStream os) {
        return new Hession2ObjectOutput(os);
    }

    public ObjectInput deserilize(InputStream is) {
        return new Hession2ObjectInput(is);
    }
}
