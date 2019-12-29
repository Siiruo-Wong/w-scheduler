package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public abstract class AbstractExecutor implements IExecutor{
     public abstract void init();
     public abstract void destroy();
}
