package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2020/1/9.
 */
public interface Sensor<T> {
    void onStart(T target);
    void onStop(T target);
}
