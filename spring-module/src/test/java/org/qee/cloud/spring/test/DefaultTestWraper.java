package org.qee.cloud.spring.test;

import lombok.Data;
import org.qee.cloud.common.annotations.AutoWraper;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.spring.objectfactory.SpringObjectFactory;

@Data
@AutoWraper
public class DefaultTestWraper implements TestSpi {

    private TestSpi testSpi;

    private ObjectFactory objectFactory;

    private SpringObjectFactory springObjectFactory;

    public DefaultTestWraper(TestSpi testSpi) {
        this.testSpi = testSpi;
    }

    @Override
    public void test(URL url, String msg) {
        testSpi.test(url, msg);
    }
}
