package org.qee.cloud.rpc.api;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class RpcInvocationHandler implements InvocationHandler, Serializable {
    private String interfaceName;

    private String methodName;

    private transient Class<?>[] parameterTypes;

    private String parameterTypesDesc;

    private Object[] arguments;

    private Map<String, Object> attachment = new HashMap<>();

    @Override
    public Map<String, Object> getAttachments() {
        return attachment;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;

    }
}
