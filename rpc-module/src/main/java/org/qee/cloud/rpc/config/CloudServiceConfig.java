package org.qee.cloud.rpc.config;


import lombok.Data;

@Data
public class CloudServiceConfig<T> {

    private String id;

    private String group;

    private String version;

    private Class<T> interfaceClass;

    private T ref;

    private boolean exported;

    //导出服务
    public void export() {
        if (exported) {
            return;
        }
        System.out.println("服务:" + ref.getClass().getName() + "服务已导出，导出接口为:" + interfaceClass.getName());
        exported = true;
    }

}
