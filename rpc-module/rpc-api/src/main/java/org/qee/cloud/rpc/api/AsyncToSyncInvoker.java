package org.qee.cloud.rpc.api;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.rpc.api.proxy.AsyncRpcResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.org.qee.cloud.rpc
 * @ClassName: AsyncToSyncInvoker
 * @Description:
 * @Date: 2021/11/23 8:18 下午
 * @Version: 1.0
 */
public class AsyncToSyncInvoker<T> implements Invoker<T> {
    private Invoker<T> invoker;

    public AsyncToSyncInvoker(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public URL getUrl() {
        return invoker.getUrl();
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    @Override
    public Result invoke(InvocationHandler invocationHandler) {
        Result result = invoker.invoke(invocationHandler);
        InvokerInvocationHandler<T> invokerInvocationHandler = (InvokerInvocationHandler<T>) invocationHandler;
        if (invokerInvocationHandler.getInvokeMode() == Result.InvokeMode.SYN.ordinal()) {
            AsyncRpcResult asyncRpcResult = (AsyncRpcResult) result;
            try {
                asyncRpcResult.getValue(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Throws.throwException(RemotingException.class, "调用被中断");
            } catch (ExecutionException e) {
                Throws.throwException(RemotingException.class, "执行异常");
            } catch (TimeoutException e) {
                Throws.throwException(RemotingException.class, "执行超时");
            }
        }
        return result;
    }
}
