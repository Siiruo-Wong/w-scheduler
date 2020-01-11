package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public interface LifecycleExecutor extends Executor {
     void init();
     void destroy();
}
