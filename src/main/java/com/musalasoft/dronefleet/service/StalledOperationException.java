package com.musalasoft.dronefleet.service;

public class StalledOperationException extends RuntimeException {

    public StalledOperationException() {
    }

    public StalledOperationException(String message) {
        super(message);
    }

    public StalledOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StalledOperationException(Throwable cause) {
        super(cause);
    }

    public StalledOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
