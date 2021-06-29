package com.indicidual.rpc.proxy;

import com.indicidual.rpc.InvocationHandler;
import com.indicidual.rpc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncRpcResult implements Result {
    CompletableFuture<AsynResult> resultCompletableFuture;
    InvocationHandler invocationHandler;

    public AsyncRpcResult(CompletableFuture<AsynResult> resultCompletableFuture, InvocationHandler invocationHandler) {
        this.resultCompletableFuture = resultCompletableFuture;
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object getValue() {
        if (resultCompletableFuture.isDone()) {
            try {
                return resultCompletableFuture.get().getValue();
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        }
        return null;

    }

    @Override
    public void setValue(Object value) {
        try {
            resultCompletableFuture.get().setValue(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasException() {
        return false;
    }

    @Override
    public Throwable getException() {
        return null;
    }

    @Override
    public void setException(Throwable t) {

    }
}
