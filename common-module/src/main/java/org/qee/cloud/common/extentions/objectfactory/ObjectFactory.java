package org.qee.cloud.common.extentions.objectfactory;

import org.qee.cloud.common.annotations.SPI;

@SPI(name = "adaptive")
public interface ObjectFactory {

    Object getObject(String name, Class<?> clzz);
}
