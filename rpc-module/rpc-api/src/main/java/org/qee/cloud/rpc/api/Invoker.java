package org.qee.cloud.rpc.api;

import org.qee.cloud.common.model.URL;

public interface Invoker<T> {

    URL getUrl();

    Class<T> getInterface();


    Result invoke(InvocationHandler invocationHandler);
}
