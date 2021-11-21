package org.qee.cloud.registry.api;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.model.URL;

@SPI(name = "zookeeper")
public interface RegistryFactory {

    @Adaptive(keys = "registry")
    RegistryCenter getRegistry(URL url);

}
