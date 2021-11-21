package org.qee.cloud.spring.config;

import org.qee.cloud.rpc.config.CloudServiceConfig;
import org.springframework.beans.factory.InitializingBean;

public class CloudServiceConfigBean<T> extends CloudServiceConfig<T> implements InitializingBean {


    // spring 内 export
    @Override
    public void afterPropertiesSet() throws Exception {
        this.export();
    }
}
