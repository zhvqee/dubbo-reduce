package com.individual.test;

import org.junit.Test;
import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;

import java.io.IOException;

public class ExtensionLoaderTest {

    @Test
    public void getExtensionLoader() throws ClassNotFoundException, IOException {
        ExtensionLoader<ObjectFactory> extensionLoader = ExtensionLoader.getExtensionLoader(ObjectFactory.class);
        assert extensionLoader != null;

    }
}
