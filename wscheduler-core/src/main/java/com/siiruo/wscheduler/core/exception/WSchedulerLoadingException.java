package com.siiruo.wscheduler.core.exception;

/**
 * Created by siiruo wong on 2019/12/23.
 */
public class WSchedulerLoadingException extends RuntimeException{
    public WSchedulerLoadingException() {
        super();
    }

    public WSchedulerLoadingException(String message) {
        super(message);
    }

    public WSchedulerLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public WSchedulerLoadingException(Throwable cause) {
        super(cause);
    }
}
