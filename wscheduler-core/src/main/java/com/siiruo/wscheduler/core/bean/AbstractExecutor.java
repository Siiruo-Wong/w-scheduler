package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public abstract class AbstractExecutor implements Executor {
     public abstract void init();
     public abstract void destroy();
}
