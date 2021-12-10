package org.qee.cloud.registry.api;

import com.qee.cloud.cluster.Directory;
import org.apache.commons.collections4.CollectionUtils;
import org.qee.cloud.common.extentions.ExtensionLoader;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.InvocationHandler;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.protocol.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.registry.api
 * @ClassName: RegistryDirectory
 * @Description:
 * @Date: 2021/11/23 1:19 下午
 * @Version: 1.0
 */
public class RegistryDirectory<T> implements Directory<T>, NotifyListener {

    private Class<T> interfaceClass;

    /**
     * key: host->ip:port
     */
    private ConcurrentHashMap<String, Invoker<T>> providerInvokersMap = new ConcurrentHashMap<>();

    private Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();

    private List<Invoker<T>> providerInvokers = new ArrayList<>();

    private URL consumerUrl;

    private Registry registry;

    public RegistryDirectory(Class<T> interfaceClass, URL consumerUrl, Registry registry) {
        this.interfaceClass = interfaceClass;
        this.consumerUrl = consumerUrl;
        this.registry = registry;
    }

    @Override
    public URL getConsumerUrl() {
        return consumerUrl;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public List<Invoker<T>> list(InvocationHandler invocationHandler) {
        return providerInvokers;
    }

    private void refreshInvoker(List<URL> providerUrls) {
        if (CollectionUtils.isEmpty(providerUrls)) {
            return;
        }
        providerInvokers = providerUrls.stream().map(this::toInvoker).collect(Collectors.toList());
    }

    private Invoker<T> toInvoker(URL url) {
        //添加引用的
        url.addParameter("protocol", consumerUrl.getProtocol());
        //添加consumer 配置，如果consumer配置已consumer为主
        url.addParameters(consumerUrl.getParameters());
        Invoker<T> invoker = protocol.refer(interfaceClass, url);
        providerInvokersMap.put(invoker.getUrl().getHostDomain(), invoker);
        return invoker;
    }

    @Override
    public void notify(List<URL> providerUrls) {
        refreshInvoker(providerUrls);
    }

    public void subscribe(URL consumserUrl) {
        registry.subscribe(consumserUrl, this);
    }
}
