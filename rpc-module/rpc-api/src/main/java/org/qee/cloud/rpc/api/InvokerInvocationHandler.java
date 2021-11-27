package org.qee.cloud.rpc.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvokerInvocationHandler<T> implements InvocationHandler {

    private Invoker<T> invoker;

    private String interfaceName;

    private Method method;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Class<?> returnTypeClass;

    private Map<String, Object> attachment = new HashMap<>();

    public InvokerInvocationHandler(Invoker<T> invoker, Method method, Object[] arguments, Class<?> returnTypeClass) {
        this.invoker = invoker;
        this.method = method;
        this.arguments = arguments;
        int parameterCount = method.getParameterCount();
        if (parameterCount > 0) {
            setParameterTypes(method.getParameterTypes());
        }
        this.returnTypeClass = returnTypeClass;
        this.interfaceName = invoker.getInterface().getName();
        attachment.put("service.group", invoker.getUrl().getParameter("service.group"));
        attachment.put("service.version", invoker.getUrl().getParameter("service.version"));
    }

    public InvokerInvocationHandler(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    @Override
    public String getMethodName() {
        return method.getName();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Map<String, Object> getAttachments() {
        return attachment;
    }


    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * 目前都为同步
     *
     * @return
     */
    public int getInvokeMode() {
        return Result.InvokeMode.SYN.ordinal();
    }
}
