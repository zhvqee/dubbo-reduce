package org.qee.cloud.remoting.api.exchange.request;

import java.util.concurrent.atomic.AtomicLong;

public class Request {

    private static AtomicLong idGenerator = new AtomicLong(0);


    private long id;

    private boolean twoWay;

    private String version;

    private Object data;

    public static long newId() {
        return idGenerator.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
