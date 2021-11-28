package org.qee.cloud.common.exceptions;

public class InjectException extends CloudException {
    public InjectException(String message) {
        super(message);
    }

    public InjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
