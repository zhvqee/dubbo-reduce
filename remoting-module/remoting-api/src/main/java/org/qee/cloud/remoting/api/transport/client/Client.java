package org.qee.cloud.remoting.api.transport.client;

import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;

import java.util.concurrent.CompletableFuture;

public interface Client {


    CompletableFuture<Response> request(Request request);
}
