package com.qee.cloud.cluster;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.rpc.api.Invoker;

/**
 * @ProjectName: qee-cloud
 * @Package: com.qee.cloud.cluster
 * @ClassName: Cluster
 * @Description:
 * @Date: 2021/11/23 2:17 下午
 * @Version: 1.0
 */
@SPI(name = "failfast")
public interface Cluster {

    @Adaptive(keys = "cluster")
    <T> Invoker<T> join(Directory<T> directory);
}
