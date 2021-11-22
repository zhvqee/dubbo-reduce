package org.qee.cloud.registry.api;

import org.qee.cloud.common.model.URL;

import java.util.List;

public interface NotifyListener {

    /**
     * 通知 urls 上的所有服务
     *
     * @param providerUrls
     */
    void notify(List<URL> providerUrls);
}
