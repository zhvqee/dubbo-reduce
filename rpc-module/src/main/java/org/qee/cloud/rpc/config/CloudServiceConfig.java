package org.qee.cloud.rpc.config;


import lombok.Data;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.api.RegistryCenterService;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.protocol.export.Exporter;
import org.qee.cloud.rpc.proxy.ProxyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CloudServiceConfig<T> {

    private String id;

    private String group;

    private String version;

    private Class<T> interfaceClass;

    private T ref;

    private boolean exported;

    private ProxyFactory proxyFactory;

    private Protocol protocol;
    private List<Exporter<T>> exporters = new ArrayList<>();

    //导出服务
    public void export() {
        if (exported) {
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        List<URL> registriesUrls = RegistryCenterService.getRegistriesUrls();
        //目前先实现一个注册中心
        // TODO: 2021/11/23
        URL registryUrl = registriesUrls.get(0);
        registryUrl.addParameters(paramMap);
        Invoker<T> invoker = proxyFactory.getInvoker(ref, interfaceClass, registryUrl);
        Exporter<T> export = protocol.export(invoker, registryUrl);
        exporters.add(export);
        System.out.println("服务:" + ref.getClass().getName() + "服务已导出，导出接口为:" + interfaceClass.getName());
        exported = true;
    }

}
