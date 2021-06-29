package com.indicidual.rpc;

public class InvokerInvocationHandler<T> implements InvocationHandler {

    private Invoker<T> invoker;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    public InvokerInvocationHandler(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
