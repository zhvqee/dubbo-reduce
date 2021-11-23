package org.qee.cloud.remoting.api.transport.client;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.transport.AbstractEndPoint;

public abstract class AbstractClient extends AbstractEndPoint implements Client {

    public AbstractClient(URL url) {
        super(url);
        try{
            doOpen();
        }catch (Exception e){

        }

        try {
            doConnect();
        } catch (
                Throwable e) {
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to bind " + getClass().getSimpleName()
                    + " on " + getLocalAddress() + ", cause: " + e.getMessage(), e);
        }
    }

    protected abstract void doOpen();

    protected abstract void doConnect() throws InterruptedException;

}
