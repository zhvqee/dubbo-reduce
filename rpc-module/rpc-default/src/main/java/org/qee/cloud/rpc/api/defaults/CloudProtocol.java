package org.qee.cloud.rpc.api.defaults;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.MethodReturnUtils;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.remoting.api.channel.Channel;
import org.qee.cloud.remoting.api.channel.DefaultFuture;
import org.qee.cloud.remoting.api.exchange.ExchangeClient;
import org.qee.cloud.remoting.api.exchange.ExchangeHandler;
import org.qee.cloud.remoting.api.exchange.ExchangeHandlerAdapter;
import org.qee.cloud.remoting.api.exchange.ExchangeServer;
import org.qee.cloud.remoting.api.exchange.Exchangers;
import org.qee.cloud.remoting.api.exchange.request.Request;
import org.qee.cloud.remoting.api.exchange.response.Response;
import org.qee.cloud.rpc.api.AsyncToSyncInvoker;
import org.qee.cloud.rpc.api.InvocationHandler;
import org.qee.cloud.rpc.api.Invoker;
import org.qee.cloud.rpc.api.InvokerInvocationHandler;
import org.qee.cloud.rpc.api.Result;
import org.qee.cloud.rpc.api.protocol.Protocol;
import org.qee.cloud.rpc.api.protocol.export.Exporter;
import org.qee.cloud.rpc.api.proxy.AsyncRpcResult;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudProtocol implements Protocol {


    private Map<String, Exporter<?>> exportMap = new ConcurrentHashMap<>();

    private Map<String, ExchangeServer> exchangeServerMap = new ConcurrentHashMap<>();

    private ExchangeHandler exchangeHandler = new ExchangeHandlerAdapter() {
        @Override
        public CompletableFuture<Object> reply(Channel channel, Object msg) throws RemotingException {
            if (msg instanceof Request) {//服务端收到请求
                Request request = (Request) msg;
                if (request.getData() instanceof InvokerInvocationHandler) {
                    InvokerInvocationHandler invocationHandler = (InvokerInvocationHandler) request.getData();
                    String interfaceName = invocationHandler.getInterfaceName();
                    String version = (String) invocationHandler.getAttachment().get("version");
                    String group = (String) invocationHandler.getAttachment().get("group");
                    Exporter<?> exporter = exportMap.get(interfaceName + ":" + group + ":" + version);
                    if (exporter == null || exporter.getInvoker() == null) {
                        Throws.throwException(RemotingException.class, "远程解析错误");
                    }
                    Invoker<?> invoker = exporter.getInvoker();
                    Result result = invoker.invoke(invocationHandler);
                    try {
                        Method method = invoker.getInterface().getMethod(invocationHandler.getMethodName(), invocationHandler.getParameterTypes());
                        boolean voidReturn = MethodReturnUtils.isVoidReturn(method);
                        if (!voidReturn) {

                            Response response = new Response();
                            response.setId(request.getId());
                            if (result.getException() != null) {
                                response.setData(result.getException());
                                response.setStatus(Response.SERVER_ERR);
                            } else {
                                response.setData(result.getValue());
                                response.setStatus(Response.OK);
                            }
                            channel.sent(response);
                        }
                    } catch (NoSuchMethodException e) {
                        Throws.throwException(RemotingException.class, "远程异常");
                    }
                    return CompletableFuture.completedFuture(result);
                }
                Throws.throwException(RemotingException.class, "远程解析错误");
            }
            Throws.throwException(RemotingException.class, "远程解析错误");
            return null;
        }

        @Override
        public void sent(Channel channel, Object message) throws RemotingException {
            channel.sent(message);
        }

        @Override
        public void received(Channel channel, Object message) throws RemotingException {
            if (message instanceof Request) {
                reply(channel, message);
            } else if (message instanceof Response) {
                DefaultFuture.received((Response) message, false);
            }
        }
    };


    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL providerUrl) {
        return new AsyncToSyncInvoker<>(getInvoker(refInterfaceClass, getExchangeClients(providerUrl), providerUrl));
    }


    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        ExchangeServer exchangeServer = openServer(url);
        Exporter<T> exporter = new Exporter<>(invoker, exchangeServer);
        exportMap.put(url.getPath() + ":" + url.getParameter("service.group") + ":" + url.getParameter("service.version"), exporter);
        return exporter;
    }

    private ExchangeServer openServer(URL url) {
        return Exchangers.bind(url, exchangeHandler);

    }

    private <T> Invoker<T> getInvoker(Class<T> refInterfaceClass, ExchangeClient[] exchangeClients, URL providerUrl) {
        return new Invoker<T>() {
            private AtomicInteger integer = new AtomicInteger();

            @Override
            public Class<T> getInterface() {
                return refInterfaceClass;
            }

            @Override
            public Result invoke(InvocationHandler invocationHandler) {
                ExchangeClient exchangeClient = null;
                if (exchangeClients.length == 1) {
                    exchangeClient = exchangeClients[0];
                } else {
                    exchangeClient = exchangeClients[integer.getAndIncrement() % exchangeClients.length];
                }
                Request request = new Request();
                request.setVersion(providerUrl.getParameter("version", "1.0"));
                request.setTwoWay(!MethodReturnUtils.isVoidClass(invocationHandler.getReturnType()));
                request.setData(invocationHandler);
                CompletableFuture<Response> completableFuture = exchangeClient.request(request);
                return new AsyncRpcResult(completableFuture, invocationHandler);
            }
        };
    }

    private ExchangeClient[] getExchangeClients(URL url) {
        int connections = url.getParameter("connections", 1);
        ExchangeClient[] exchangeClient = new ExchangeClient[connections];
        for (int i = 0; i < connections; i++) {
            exchangeClient[i] = Exchangers.connect(url, exchangeHandler);
        }
        return exchangeClient;

    }


}
