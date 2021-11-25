package org.qee.cloud.rpc.api.protocol.export;


import org.qee.cloud.rpc.api.Invoker;

public class Exporter<T> {

    private Invoker<T> invoker;

    public Exporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }
}
