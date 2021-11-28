package org.qee.cloud.rpc.api.defaults;

import org.qee.cloud.common.exceptions.RemotingException;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.common.utils.Asserts;
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
import org.qee.cloud.rpc.api.Result;
import org.qee.cloud.rpc.api.RpcInvocationHandler;
import org.qee.cloud.rpc.api.protocol.Protocol;
import org.qee.cloud.rpc.api.protocol.export.Exporter;
import org.qee.cloud.rpc.api.proxy.AsyncRpcResult;
import org.qee.cloud.rpc.api.utils.ReflectUtils;

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
            Asserts.assertTrue(msg instanceof Request, RemotingException.class, "远程解析异常,msg 类型不是 org.qee.cloud.remoting.api.exchange.request.Request");
            Request request = (Request) msg;
            if (!(request.getData() instanceof RpcInvocationHandler)) {
                throw new RemotingException("远程数据解析异常,request.data 数据类型不是 org.qee.cloud.rpc.api.RpcInvocationHandler");
            }
            RpcInvocationHandler invocationHandler = (RpcInvocationHandler) request.getData();
            String interfaceName = invocationHandler.getInterfaceName();
            String version = (String) invocationHandler.getAttachment().get("service.version");
            String group = (String) invocationHandler.getAttachment().get("service.group");
            Exporter<?> exporter = exportMap.get(interfaceName + ":" + group + ":" + version);
            Asserts.assertTrue(exporter != null && exporter.getInvoker() != null, RemotingException.class, "远程解析错误:" + invocationHandler);
            Invoker<?> invoker = exporter.getInvoker();
            String parameterTypesDesc = invocationHandler.getParameterTypesDesc();
            if (parameterTypesDesc != null && parameterTypesDesc.length() > 0) {
                String[] parameterTypesDescs = parameterTypesDesc.split(";");
                Class<?>[] classArr = new Class[parameterTypesDescs.length];
                int i = 0;
                for (String pt : parameterTypesDescs) {
                    try {
                        classArr[i++] = ReflectUtils.getClass(pt);
                    } catch (ClassNotFoundException e) {
                        throw new RemotingException("获取参数类型异常,paramterType:" + pt, e);
                    }
                }
                invocationHandler.setParameterTypes(classArr);
            }

            Result result = invoker.invoke(invocationHandler);
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
            return CompletableFuture.completedFuture(result);
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
        exchangeServerMap.put(url.getPath() + ":" + url.getParameter("service.group") + ":" + url.getParameter("service.version"), exchangeServer);
        return exporter;
    }

    private ExchangeServer openServer(URL url) {
        return Exchangers.bind(url, exchangeHandler);

    }

    private <T> Invoker<T> getInvoker(Class<T> refInterfaceClass, ExchangeClient[] exchangeClients, URL providerUrl) {
        return new Invoker<T>() {
            private final AtomicInteger id = new AtomicInteger();

            @Override
            public URL getUrl() {
                return providerUrl;
            }

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
                    exchangeClient = exchangeClients[id.getAndIncrement() % exchangeClients.length];
                }
                Request request = new Request();
                request.setVersion(providerUrl.getParameter("version", "1.0"));
                request.setTwoWay(true);
                RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler();

                rpcInvocationHandler.setAttachment(invocationHandler.getAttachments());
                rpcInvocationHandler.setInterfaceName(invocationHandler.getInterfaceName());
                rpcInvocationHandler.setMethodName(invocationHandler.getMethodName());
                rpcInvocationHandler.setParameterTypes(invocationHandler.getParameterTypes());
                if (invocationHandler.getParameterTypes() != null) {
                    String paramterTypeDesc = "";
                    for (Class<?> pc : invocationHandler.getParameterTypes()) {
                        paramterTypeDesc += ReflectUtils.getDesc(pc);
                    }
                    rpcInvocationHandler.setParameterTypesDesc(paramterTypeDesc);
                }
                rpcInvocationHandler.setArguments(invocationHandler.getArguments());
                request.setData(rpcInvocationHandler);

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
