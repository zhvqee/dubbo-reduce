package org.qee.cloud.registry.api;

import com.qee.cloud.cluster.Cluster;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.NetUtils;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.protocol.Protocol;
import org.qee.cloud.rpc.api.protocol.export.Exporter;


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

    private Protocol protocol;

    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL url) {
        Registry registry = registryFactory.getRegistry(url);
        URL consumserUrl = getConsumerUrl(url, refInterfaceClass);
        RegistryDirectory<T> directory = new RegistryDirectory<>(refInterfaceClass, consumserUrl, registry);
        directory.subscribe(consumserUrl);
        return cluster.join(directory);
    }

    private <T> URL getConsumerUrl(URL url, Class<T> refInterfaceClass) {
        URL consumerUrl = URL.builder().protocol(url.getParameter("service.registry.protocol"))
                .host(NetUtils.getLocalInetAddress().getHostName()).port(Integer.parseInt(url.getParameter("service.registry.port")))
                .path(refInterfaceClass.getName()).build();
        consumerUrl.addParameter("service.group", url.getParameter("service.group", "*"));
        consumerUrl.addParameter("service.version", url.getParameter("service.version", "*"));
        consumerUrl.addParameter("timeout", url.getParameter("timeout"));
        return consumerUrl;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        Registry registry = registryFactory.getRegistry(url);
        URL providerUrl = getProviderUrl(url, invoker);
        registry.register(providerUrl);
        return protocol.export(invoker, providerUrl);
    }

    private <T> URL getProviderUrl(URL url, Invoker<T> invoker) {
        URL providerUrl = URL.builder().protocol(url.getParameter("service.registry.protocol"))
                .host(NetUtils.getLocalInetAddress().getHostName())
                .path(invoker.getInterface().getName())
                .port(Integer.parseInt(url.getParameter("service.registry.port")))
                .build();
        providerUrl.addParameter("service.group", url.getParameter("service.group", "*"));
        providerUrl.addParameter("service.version", url.getParameter("service.version", "*"));
        return providerUrl;
    }

    public void setRegistryFactory(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
