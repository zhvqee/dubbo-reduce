package org.qee.cloud.registry.api;

import org.qee.cloud.common.exceptions.RegistryException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.Throws;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistryFactory implements RegistryFactory {

    private static final String REGISTRY_PROTOCOL = "registry";

    private static ConcurrentHashMap<String, RegistryCenter> HOST_KEY_REGISTRY_MAP = new ConcurrentHashMap<>();

    //  dubbo url
    //registry://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService?application=dubbo-demo-annotation-provider&dubbo=2.0.2&export=dubbo%3A%2F%2F192.168.1.4%3A20880%2Forg.apache.dubbo.demo.DemoService%3Fanyhost%3Dtrue%26application%3Ddubbo-demo-annotation-provider%26bind.ip%3D192.168.1.4%26bind.port%3D20880%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.apache.dubbo.demo.DemoService%26metadata-type%3Dremote%26methods%3DsayHello%2CsayHelloAsync%26pid%3D2787%26release%3D%26side%3Dprovider%26timestamp%3D1637499188879&pid=2787&registry=zookeeper&timestamp=1637499188869
    //zookeeper://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService?application=dubbo-demo-annotation-provider&dubbo=2.0.2&export=dubbo%3A%2F%2F192.168.1.4%3A20880%2Forg.apache.dubbo.demo.DemoService%3Fanyhost%3Dtrue%26application%3Ddubbo-demo-annotation-provider%26bind.ip%3D192.168.1.4%26bind.port%3D20880%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.apache.dubbo.demo.DemoService%26metadata-type%3Dremote%26methods%3DsayHello%2CsayHelloAsync%26pid%3D2787%26release%3D%26side%3Dprovider%26timestamp%3D1637499188879&pid=2787&timestamp=1637499188869

    //dubbo 是通过协议进行Adaptive，而这里采用的是参数xxxx?//registry=xxx 来适配
    //比如要zk,则 registry=zookeeper==> registry://127.0.0.1:2181？registry=zookeeper
    @Override
    public RegistryCenter getRegistry(URL url) {

        String key = getHostRegistryKey(url);
        RegistryCenter registryCenter = HOST_KEY_REGISTRY_MAP.get(key);
        if (registryCenter != null) {
            return registryCenter;
        }
        registryCenter = doCreateRegistry(url);
        if (registryCenter == null) {
            Throws.throwException(RegistryException.class, "创建注册中心异常");
        }
        HOST_KEY_REGISTRY_MAP.put(key, registryCenter);
        return registryCenter;
    }

    public String getHostRegistryKey(URL url) {
        String registry = url.getParameter("registry", "zookeeper");
        URL build = URL.builder().protocol(registry).host(url.getHost()).port(url.getPort()).build();
        return build.toShortUrl();
    }

    public abstract RegistryCenter doCreateRegistry(URL url);

}
