package org.qee.cloud.rpc.protocol.export;

import org.qee.cloud.rpc.Invoker;

public class Exporter<T> {

    private Invoker<T> invoker;

    public Exporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }
}
