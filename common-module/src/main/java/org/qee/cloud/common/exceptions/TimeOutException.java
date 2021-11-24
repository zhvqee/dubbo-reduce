package org.qee.cloud.common.exceptions;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.exceptions
 * @ClassName: TimeOutException
 * @Description:
 * @Date: 2021/11/24 1:04 下午
 * @Version: 1.0
 */
public class TimeOutException extends CloudException {

    public TimeOutException(String message) {
        super(message);
    }
}
