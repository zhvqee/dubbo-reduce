package org.qee.cloud.rpc.proxy;

import org.qee.cloud.rpc.Result;

public class AsynResult implements Result {

    private Object value;

    private boolean hasException;

    private Throwable throwable;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean hasException() {
        return hasException;
    }

    @Override
    public Throwable getException() {
        return throwable;
    }

    @Override
    public void setException(Throwable t) {
        this.throwable = t;
        this.hasException = true;
    }
}
