package org.qee.cloud.rpc.config;

import com.qee.cloud.cluster.Cluster;
import lombok.Data;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.api.RegistryCenterService;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.proxy.ProxyFactory;

import java.util.List;

@Data
public class CloudReferenceConfig<T> {

    private String id;

    private String interfaceName;

    private Class<T> interfaceClass;

    private String version;

    private String group;

    private String mock;

    private boolean check;

    private T ref;

    private boolean inited;

    private ProxyFactory proxyFactory;

    private Cluster cluster;

    private Protocol protocol;

    public T get() {
        if (inited && ref != null) {
            return ref;
        }
        List<URL> registriesUrls = RegistryCenterService.getRegistriesUrls();
        //目前先实现一个注册中心
        // TODO: 2021/11/23  
        URL registryUrl = registriesUrls.get(0);
        Invoker<T> invoker = protocol.refer(interfaceClass, registryUrl);
        ref = proxyFactory.getProxy(invoker);
        return ref;
    }
}
