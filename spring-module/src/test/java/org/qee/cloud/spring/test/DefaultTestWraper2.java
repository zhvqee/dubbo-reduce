package org.qee.cloud.spring.test;

import lombok.Data;
import org.qee.cloud.common.annotations.AutoWraper;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.spring.objectfactory.SpringObjectFactory;

@Data
@AutoWraper(order = 2)
public class DefaultTestWraper2 implements TestSpi {

    private TestSpi testSpi;

    private ObjectFactory objectFactory;

    private SpringObjectFactory springObjectFactory;

    public DefaultTestWraper2(TestSpi testSpi) {
        this.testSpi = testSpi;
    }

    @Override
    public void test(URL url, String msg) {
        testSpi.test(url, msg);
    }
}
