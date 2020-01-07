package com.siiruo.wscheduler.core.exception;

/**
 * Created by siiruo wong on 2020/1/7.
 */
public class WSchedulerRpcException extends RuntimeException {
    public WSchedulerRpcException(String message) {
        super(message);
    }

    public WSchedulerRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
