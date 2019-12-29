package com.siiruo.wscheduler.core.exception;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class WSchedulerExecutorConflictException extends RuntimeException{
    private String first;
    private String second;

    public WSchedulerExecutorConflictException(String first,String second) {
        super(String.format("there's a conflict between {} and {}",first,second));
        this.first=first;
        this.second=second;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }
}
