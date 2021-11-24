package org.qee.cloud.remoting.api.exchange.response;

public class Response {

    public static final int OK = 200;
    public static final int CLIENT_TIMEOUT = 401;

    public static final int SERVER_TIMEOUT = 402;

    public static final int SERVER_ERR = 403;


    private long id;

    private Object data;

    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
