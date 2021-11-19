package org.qee.cloud.rpc.proxy;

import org.qee.cloud.rpc.InvocationHandler;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    @Override
    public Result invoke(InvocationHandler invocationHandler) {
        Object resultValue = doInvoke(invocationHandler);
        CompletableFuture<Object> feture = wraper(resultValue);

        CompletableFuture<AsynResult> resultCompletableFuture = feture.handle((obj, t) -> {
            AsynResult asynResult = new AsynResult();
            if (t != null) {
                if (t instanceof CompletionException) {
                    asynResult.setException(t.getCause());
                } else {
                    asynResult.setException(t);
                }
            } else {
                asynResult.setValue(obj);
            }
            return asynResult;
        });

        return new AsyncRpcResult(resultCompletableFuture, invocationHandler);
    }

    protected CompletableFuture<Object> wraper(Object resultValue) {
        if (resultValue instanceof CompletableFuture) {
            return (CompletableFuture<Object>) resultValue;
        }
        // 异步之后填写
        // TODO: 2021/6/28

        return CompletableFuture.completedFuture(resultValue);
    }


    protected abstract Object doInvoke(InvocationHandler invocationHandler);
}
