package org.qee.cloud.registry.zookeeper;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.api.AbstractRegistryFactory;
import org.qee.cloud.registry.api.RegistryCenter;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    public RegistryCenter doCreateRegistry(URL url) {
        return new ZookeeperRegistryCenter(url);
    }

}
