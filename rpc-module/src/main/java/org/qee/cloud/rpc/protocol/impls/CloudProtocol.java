package org.qee.cloud.rpc.protocol.impls;

import org.qee.cloud.common.model.URL;
import org.qee.cloud.rpc.Invoker;
import org.qee.cloud.rpc.protocol.Protocol;
import org.qee.cloud.rpc.protocol.export.Exporter;

public class CloudProtocol implements Protocol {
    @Override
    public <T> Invoker<T> refer(Class<T> refInterfaceClass, URL url) {
        return null;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }
}
