package org.qee.cloud.spring.test;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.model.URL;

@SPI(name = "test")
public interface TestSpi {

    @Adaptive(keys = "123")
    public  void test(URL url,String msg);
}
