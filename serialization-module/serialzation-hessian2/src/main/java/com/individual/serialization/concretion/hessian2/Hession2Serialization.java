package com.individual.serialization.concretion.hessian2;

import com.individual.serialization.api.ObjectInput;
import com.individual.serialization.api.ObjectOutput;
import com.individual.serialization.api.Serialization;

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
