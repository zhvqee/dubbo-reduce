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
        int allWeight = 0;
        for (Invoker<T> invoker : invokerList) {
            allWeight += getWeight(invoker);
        }
        int weight = ThreadLocalRandom.current().nextInt(allWeight);
        int i = 0;
        for (; i < invokerList.size(); i++) {
            weight -= getWeight(invokerList.get(i));
            if (weight <= 0) {
                break;
            }
        }
        return invokerList.get(i);
    }

    private <T> int getWeight(Invoker<T> invoker) {
        return invoker.getUrl().getParameter("weight", 100);
    }
}
