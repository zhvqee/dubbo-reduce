package com.qee.cloud.loadbalance;

import org.qee.cloud.common.annotations.Adaptive;
import org.qee.cloud.common.annotations.SPI;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.Invoker;

import java.util.List;

@SPI(name = "random")
public interface LoadBalance {

    @Adaptive(keys = "loadbalance")
    <T> Invoker<T> select(List<Invoker<T>> invokerList, URL url);
}
