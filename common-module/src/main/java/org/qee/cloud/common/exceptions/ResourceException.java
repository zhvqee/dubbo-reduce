package org.qee.cloud.common.exceptions;

/**
 * @ProjectName: qee-cloud
 * @Package: com.individual.common.exceptions
 * @ClassName: ResourceException
 * @Description:
 * @Date: 2021/11/18 4:31 下午
 * @Version: 1.0
 */
public class ResourceException extends RuntimeException {
    public ResourceException(String message) {
        super(message);
    }
}
