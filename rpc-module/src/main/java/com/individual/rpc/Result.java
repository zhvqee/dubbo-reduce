package com.individual.rpc;

public interface Result {

    Object getValue();

    void setValue(Object value);

    boolean hasException();

    Throwable getException();

    void setException(Throwable t);
}
