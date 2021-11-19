package org.qee.cloud.common.exceptions;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.exceptions
 * @ClassName: CloudException
 * @Description:
 * @Date: 2021/11/19 5:01 下午
 * @Version: 1.0
 */
public class CloudException extends RuntimeException {
    public CloudException(String message) {
        super(message);
    }


    public CloudException(String message, Throwable cause) {
        super(message, cause);
    }
}
