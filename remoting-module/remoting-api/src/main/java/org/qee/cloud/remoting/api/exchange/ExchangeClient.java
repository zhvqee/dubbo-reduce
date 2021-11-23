package org.qee.cloud.remoting.api.exchange;

import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.remoting.api.transport.client.Client;

public interface ExchangeClient extends Client {

    Response request(Request request);
}
