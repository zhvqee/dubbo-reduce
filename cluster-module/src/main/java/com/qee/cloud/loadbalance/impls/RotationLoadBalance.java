package com.qee.cloud.loadbalance.impls;

import com.qee.cloud.loadbalance.LoadBalance;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.api.Invoker;

import java.util.List;

/**
 * @ProjectName: qee-cloud
 * @Package: com.qee.cloud.loadbalance.impls
 * @ClassName: RotationLoadBalance
 * @Description:
 * @Date: 2021/12/2 5:13 下午
 * @Version: 1.0
 */
public class RotationLoadBalance  implements LoadBalance {
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url) {
        return null;
    }


}
