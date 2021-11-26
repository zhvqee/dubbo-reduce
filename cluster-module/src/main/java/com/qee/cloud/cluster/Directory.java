package com.qee.cloud.cluster;



import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.InvocationHandler;
import org.qee.cloud.rpc.api.Invoker;

import java.util.List;

/**
 * @ProjectName: qee-cloud
 * @Package: com.qee.cloud.cluster
 * @ClassName: Directory
 * @Description:
 * @Date: 2021/11/23 1:13 下午
 * @Version: 1.0
 */
public interface Directory<T> {

    URL getConsumerUrl();

    Class<T> getInterface();

    List<Invoker<T>> list(InvocationHandler invocationHandler);


}
