package org.qee.cloud.spring.test;

import org.junit.Test;
import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.qee.cloud.common.model.URL;

import java.io.IOException;

public class SpringObjectFacotoryTest {

    @Test
    public void getExtensionLoader() throws ClassNotFoundException, IOException {
        ExtensionLoader<ObjectFactory> extensionLoader = ExtensionLoader.getExtensionLoader(ObjectFactory.class);
        assert extensionLoader != null;
    }

    @Test
    public void getExtensionLoaderOther() throws ClassNotFoundException, IOException {
        ExtensionLoader<TestSpi> extensionLoader = ExtensionLoader.getExtensionLoader(TestSpi.class);
        assert extensionLoader != null;

        TestSpi adaptiveExtension = extensionLoader.getAdaptiveExtension();
        TestSpi defaultExtension = extensionLoader.getDefaultExtension();
        TestSpi aDefault = extensionLoader.getExtension("default");

        adaptiveExtension.test(URL.valueOf("https://blog.csdn.net/"), "1234");

        System.out.println("=======");
        defaultExtension.test(URL.valueOf("https://blog.csdn.net/"), "1234");
        aDefault.test(URL.valueOf("https://blog.csdn.net/"), "1234");

    }


    @Test
    public void getExtensionLoaderWraper() throws ClassNotFoundException, IOException {
        ExtensionLoader<TestSpi> extensionLoader = ExtensionLoader.getExtensionLoader(TestSpi.class);
        assert extensionLoader != null;

        TestSpi adaptiveExtension = extensionLoader.getAdaptiveExtension();

        adaptiveExtension.test(URL.valueOf("https://blog.csdn.net/"), "1234");

        TestSpi aDefault = extensionLoader.getExtension("default");
        aDefault.test(URL.valueOf("https://blog.csdn.net/"), "1234");

    }
}
