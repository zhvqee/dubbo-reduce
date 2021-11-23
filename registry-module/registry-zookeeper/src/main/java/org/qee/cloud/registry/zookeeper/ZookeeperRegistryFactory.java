package org.qee.cloud.registry.zookeeper;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.api.AbstractRegistryFactory;
import org.qee.cloud.registry.api.Registry;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    public Registry doCreateRegistry(URL url) {
        return new ZookeeperRegistry(url);
    }

}
