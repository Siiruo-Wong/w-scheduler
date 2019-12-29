package com.siiruo.wscheduler.client.bean;

import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.ExecutorParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class SingleExecutor extends AbstractExecutor {
    private Object target;
    private String name;
    private Method execute;
    private Method before;
    private Method after;
    private Method init;
    private Method destroy;

    private volatile boolean initialized;
    private volatile boolean destroyed;

    public SingleExecutor() {
    }

    public SingleExecutor(Object target, String name, Method execute) {
        this.target = target;
        this.name = name;
        this.execute = execute;
    }

    public SingleExecutor(Object target, String name, Method execute, Method before, Method after, Method init, Method destroy) {
        this.target = target;
        this.name = name;
        this.execute = execute;
        this.before = before;
        this.after = after;
        this.init = init;
        this.destroy = destroy;
    }

    private synchronized void internalExecute(ExecutorParameter parameter){
        if (!initialized) {
            init();
        }
        try {
            if (before!=null) {
                before.setAccessible(true);
                before.invoke(target);
            }
            execute.setAccessible(true);
            execute.invoke(target,parameter);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }finally {
            if (after!=null) {
                try {
                    after.setAccessible(true);
                    after.invoke(target);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }

    @Override
    public void execute(ExecutorParameter parameter) {
        internalExecute(parameter);
    }

    @Override
    public void init() {
        if (init==null|| initialized) {
            return;
        }
        try {
            init.setAccessible(true);
            init.invoke(target);
            initialized =true;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    @Override
    public void destroy() {
        if (destroy==null||destroyed) {
            return;
        }
        try {
            destroy.setAccessible(true);
            destroy.invoke(target);
            destroyed=true;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    public Object getTarget() {
        return target;
    }

    public String getName() {
        return name;
    }
}
