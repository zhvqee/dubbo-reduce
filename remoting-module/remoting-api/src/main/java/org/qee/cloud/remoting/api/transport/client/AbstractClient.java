package org.qee.cloud.remoting.api.transport.client;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.remoting.api.transport.AbstractEndPoint;

public abstract class AbstractClient extends AbstractEndPoint implements Client {

    public AbstractClient(URL url) {
        super(url);
    }


}
