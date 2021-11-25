package com.qee.cloud.loadbalance.impls;

import com.qee.cloud.loadbalance.LoadBalance;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.Invoker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance implements LoadBalance {


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokerList, URL url) {
        if (invokerList.size() == 1) {
            return invokerList.get(0);
        }
        return invokerList.get(ThreadLocalRandom.current().nextInt(invokerList.size()));
    }
}
