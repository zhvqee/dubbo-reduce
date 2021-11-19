package org.qee.cloud.common.extentions.objectfactory;

import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.extentions.ExtensionLoader;

public class SpiObjectFactory implements ObjectFactory {
    @Override
    public Object getObject(String name, Class<?> clzz) {
        if (clzz.isInterface() && clzz.isAnnotationPresent(SPI.class)) {
            return ExtensionLoader.getExtensionLoader(clzz).getAdaptiveExtension();
        }
        return null;
    }
}
