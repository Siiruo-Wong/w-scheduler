package com.siiruo.wscheduler.core.exception;

/**
 * Created by siiruo wong on 2020/1/7.
 */
public class WSchedulerRemoteException extends RuntimeException {
    public WSchedulerRemoteException(String message) {
        super(message);
    }

    public WSchedulerRemoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
