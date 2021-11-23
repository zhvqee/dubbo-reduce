package org.qee.cloud.registry.api;

import com.qee.cloud.cluster.Cluster;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.NetUtils;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.protocol.export.Exporter;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.registry.api
 * @ClassName: RegistryProtocol
 * @Description:
 * @Date: 2021/11/23 3:26 下午
 * @Version: 1.0
 */
public class RegistryProtocol implements Protocol {


    private RegistryFactory registryFactory;

    private Cluster cluster;

    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL url) {
        Registry registry = registryFactory.getRegistry(url);
        URL consumserUrl = getConsumerUrl(url);
        RegistryDirectory<T> directory = new RegistryDirectory<>(refInterfaceClass, consumserUrl, registry);
        directory.subscribe(consumserUrl);
        return cluster.join(directory);
    }

    private URL getConsumerUrl(URL url) {
        return URL.builder().protocol(url.getParameter("registry.protocol")).host(NetUtils.getLocalInetAddress().getHostName()).port(Integer.parseInt(url.getParameter("registry.port"))).build();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }
}
