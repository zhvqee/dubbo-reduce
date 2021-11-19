package org.qee.cloud.spring.test;

import lombok.Data;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.qee.cloud.common.model.URL;

@Data
public class DefaultTest implements TestSpi {

    private ObjectFactory objectFactory;

    private TestSpi testSpi;


    @Override
    public void test(URL url, String msg) {
        System.out.println(msg);
    }
}
