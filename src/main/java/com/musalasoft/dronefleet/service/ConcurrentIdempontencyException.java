package com.musalasoft.dronefleet.service;

public class ConcurrentIdempontencyException extends RuntimeException {

    public ConcurrentIdempontencyException() {
    }

    public ConcurrentIdempontencyException(String message) {
        super(message);
    }

    public ConcurrentIdempontencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentIdempontencyException(Throwable cause) {
        super(cause);
    }

    public ConcurrentIdempontencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
