/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qee.cloud.remoting.api.channel;


import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;


/**
 * DefaultFuture.
 */
public class DefaultFuture extends CompletableFuture<Response> {


    private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap<>();

    private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>();


    private final Long id;

    private final Channel channel;

    private final Request request;


    private DefaultFuture(Channel channel, Request request) {
        this.channel = channel;
        this.request = request;
        this.id = request.getId();
        // put into waiting map.
        FUTURES.put(id, this);
        CHANNELS.put(id, channel);
    }


    /**
     * @param channel channel
     * @param request the request
     */
    public static DefaultFuture newFuture(Channel channel, Request request) {
        return new DefaultFuture(channel, request);
    }

    public static void received(Response response, boolean timeout) {
        try {
            DefaultFuture future = FUTURES.remove(response.getId());
            if (future != null) {
                future.doReceived(response);
            } else {
                // TODO: 2021/11/23 超时功能 
            }
        } finally {
            CHANNELS.remove(response.getId());
        }
    }

  /*  @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        Response errorResult = new Response(id);
        errorResult.setStatus(Response.CLIENT_ERROR);
        errorResult.setErrorMessage("request future has been canceled.");
        this.doReceived(errorResult);
        FUTURES.remove(id);
        CHANNELS.remove(id);
        return true;
    }*/


    private void doReceived(Response res) {
        if (res == null) {
            throw new IllegalStateException("response cannot be null");
        }
        if (res.getStatus() == Response.OK) {
            this.complete(res);
        } else if (res.getStatus() == Response.CLIENT_TIMEOUT) {
            this.completeExceptionally(new TimeoutException(res.getErrorMessage()));
        } else if (res.getStatus() == Response.SERVER_TIMEOUT) {
            this.completeExceptionally(new TimeoutException(res.getErrorMessage()));
        } else {
            this.completeExceptionally(new RemotingException(res.getErrorMessage()));
        }
    }
}
