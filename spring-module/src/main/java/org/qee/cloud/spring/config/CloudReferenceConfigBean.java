package org.qee.cloud.spring.config;

import org.qee.cloud.rpc.api.config.CloudReferenceConfig;
import org.springframework.beans.factory.FactoryBean;

public class CloudReferenceConfigBean<T>  extends CloudReferenceConfig<T> implements FactoryBean<T> {
    @Override
    public T getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
