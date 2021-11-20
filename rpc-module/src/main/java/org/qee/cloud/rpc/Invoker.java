package org.qee.cloud.rpc;

import org.qee.cloud.common.model.URL;

public interface Invoker<T> {

    default URL getUrl() {
        return URL.valueOf("localhost:8888");
    }

    Class<T> getInterface();


    Result invoke(InvocationHandler invocationHandler);
}
