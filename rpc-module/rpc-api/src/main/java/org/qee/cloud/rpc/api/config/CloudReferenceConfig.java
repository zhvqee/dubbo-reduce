package org.qee.cloud.rpc.api.config;

import lombok.Data;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.RegistryCenterService;
import org.qee.cloud.rpc.api.protocol.Protocol;
import org.qee.cloud.rpc.api.proxy.ProxyFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Protocol protocol;

    public T get() {
        if (inited && ref != null) {
            return ref;
        }
        Map<String, String> paramMap = new HashMap<>();
        List<URL> registriesUrls = RegistryCenterService.getRegistriesUrls();
        //目前先实现一个注册中心
        // TODO: 2021/11/23  
        URL registryUrl = registriesUrls.get(0);
        registryUrl.addParameters(paramMap);
        Invoker<T> invoker = protocol.refer(interfaceClass, registryUrl);
        ref = proxyFactory.getProxy(invoker);
        return ref;
    }
}
