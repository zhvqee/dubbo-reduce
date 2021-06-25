package com.individual.remoting.api.transport.client;

import com.individual.common.model.URL;
import com.individual.remoting.api.transport.AbstractEndPoint;

public abstract class AbstractClient extends AbstractEndPoint implements Client {

    public AbstractClient(URL url) {
        super(url);
    }
}
