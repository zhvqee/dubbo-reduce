package com.qee.cloud.cluster.impls;

import com.qee.cloud.cluster.Cluster;
import com.qee.cloud.cluster.Directory;
import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.rpc.api.Invoker;

public class FailFastCluster implements Cluster {
    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        try {
            return new FailFastClusterInvoker<>(directory);
        } catch (Exception e) {
            Throws.throwException(RemotingException.class, "fail fast invoke :" + directory.getInterface());
        }
        return null;
    }
}
