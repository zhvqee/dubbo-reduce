package org.qee.cloud.rpc.api.proxy;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.rpc.api.InvocationHandler;
import org.qee.cloud.rpc.api.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncRpcResult implements Result {
    CompletableFuture<Response> responseCompletableFuture;
    InvocationHandler invocationHandler;

    public AsyncRpcResult(CompletableFuture<Response> responseCompletableFuture, InvocationHandler invocationHandler) {
        this.responseCompletableFuture = responseCompletableFuture;
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object getValue() {
        if (responseCompletableFuture.isDone()) {
            try {
                return responseCompletableFuture.get().getData();
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        }
        return null;

    }

    public Object getValue(long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return responseCompletableFuture.get(timeout, timeUnit).getData();
    }

    @Override
    public void setValue(Object value) {
        try {
            responseCompletableFuture.get().setData(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasException() {
        return getValue() instanceof Exception;
    }

    @Override
    public Throwable getException() {
        return (Throwable) getValue();
    }

    @Override
    public void setException(Throwable t) {
        try {
            if (responseCompletableFuture.isDone()) {
                responseCompletableFuture.get().setData(t);
            } else {
                Response appResponse = new Response();
                appResponse.setData(t);
                responseCompletableFuture.complete(appResponse);
            }
        } catch (Exception e) {
            throw new RemotingException("获取结果异常", e);
        }

    }
}
