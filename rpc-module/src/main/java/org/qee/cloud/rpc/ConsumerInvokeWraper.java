package org.qee.cloud.rpc;

import com.qee.cloud.cluster.Cluster;
import com.qee.cloud.cluster.Directory;
import org.qee.cloud.common.model.URL;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.rpc
 * @ClassName: ConsumerInvokeWraper
 * @Description:
 * @Date: 2021/11/23 2:39 下午
 * @Version: 1.0
 */
public class ConsumerInvokeWraper<T> implements Invoker<T> {

    private Class<T> interfaceClass;

    private Directory<T> directory;

    private Cluster<T> cluster;

    private URL consumerUrl;

    public ConsumerInvokeWraper(Class<T> interfaceClass, Directory<T> directory, Cluster<T> cluster, URL consumerUrl) {
        this.interfaceClass = interfaceClass;
        this.directory = directory;
        this.cluster = cluster;
        this.consumerUrl = consumerUrl;
    }

    @Override
    public URL getUrl() {
        return consumerUrl;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public Result invoke(InvocationHandler invocationHandler) {
        return null;
    }
}
