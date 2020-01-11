package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2020/1/11.
 */
public interface Dispatcher<I,O> {
    boolean matches(I input);
    O dispatch(I input);
}
