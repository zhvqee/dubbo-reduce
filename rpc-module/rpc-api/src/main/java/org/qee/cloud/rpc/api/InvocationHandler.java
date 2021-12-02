package org.qee.cloud.rpc.api;

import java.util.Map;

public interface InvocationHandler {

    String getInterfaceName();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

    Map<String,Object> getAttachments();

}
