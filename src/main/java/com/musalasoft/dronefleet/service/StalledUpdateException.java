package com.musalasoft.dronefleet.service;

public class StalledUpdateException extends RuntimeException {

    public StalledUpdateException() {
    }

    public StalledUpdateException(String message) {
        super(message);
    }

    public StalledUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public StalledUpdateException(Throwable cause) {
        super(cause);
    }

    public StalledUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
