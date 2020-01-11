package com.siiruo.wscheduler.core.exception;

/**
 * Created by siiruo wong on 2020/1/11.
 */
public class WSchedulerExecutingException extends RuntimeException{
    public WSchedulerExecutingException() {
    }

    public WSchedulerExecutingException(String message) {
        super(message);
    }

    public WSchedulerExecutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public WSchedulerExecutingException(Throwable cause) {
        super(cause);
    }

    public WSchedulerExecutingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
