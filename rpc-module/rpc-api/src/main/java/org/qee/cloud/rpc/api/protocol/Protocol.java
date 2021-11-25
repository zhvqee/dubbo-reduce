package org.qee.cloud.rpc.api.protocol;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.protocol.export.Exporter;


/**
 * 远程调用协议,
 * <p>
 * 1) 客服端 通过 refer 方法 引用 远程的代理服务
 * 2） 服务端 通过 export 方法 暴露
 */
@SPI(name = "cloud")
public interface Protocol {

    @Adaptive(keys = "protocol")
    <T> Invoker<T> refer(Class<T> refInterfaceClass, URL url);

    @Adaptive(keys = "protocol")
    <T> Exporter<T> export(Invoker<T> invoker, URL url);


}
