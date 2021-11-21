package org.qee.cloud.registry.api;

import org.qee.cloud.common.model.URL;

public interface RegistryCenter {

    void register(URL url);

    void unregister(URL url);

    void subscribe(URL url, NotifyListener listener);

    void unsubscribe(URL url, NotifyListener listener);

}
