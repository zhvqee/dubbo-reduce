package com.individual.test;

import org.qee.cloud.common.extentions.ResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @ProjectName: qee-cloud
 * @Package: com.individual.test
 * @ClassName: ResourceLoaderTest
 * @Description:
 * @Date: 2021/11/18 4:56 下午
 * @Version: 1.0
 */
public class ResourceLoaderTest {

    @Test
    public void loadExtension() throws ClassNotFoundException, IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("com.test.TestService");
        Properties properties = ResourceLoader.loadExtension(resource);
        System.out.println(properties);

    }
}
