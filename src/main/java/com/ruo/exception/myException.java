package com.ruo.exception;

public class myException extends RuntimeException {
    public myException() {
        super();
    }

    public myException(String message) {
        super(message);
    }

    public myException(String message, Throwable cause) {
        super(message, cause);
    }

    public myException(Throwable cause) {
        super(cause);
    }

    protected myException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
