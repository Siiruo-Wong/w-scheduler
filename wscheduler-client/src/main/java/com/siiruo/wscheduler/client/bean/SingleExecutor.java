package com.siiruo.wscheduler.client.bean;

import com.siiruo.wscheduler.core.bean.LifecycleExecutor;
import com.siiruo.wscheduler.core.bean.ExecuteParameter;
import com.siiruo.wscheduler.core.exception.WSchedulerExecutingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class SingleExecutor implements LifecycleExecutor {
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

    private synchronized void internalExecute(ExecuteParameter parameter) throws WSchedulerExecutingException {
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
            throw new WSchedulerExecutingException(e);
        } catch (InvocationTargetException e) {
            throw new WSchedulerExecutingException(e);
        }finally {
            if (after!=null) {
                try {
                    after.setAccessible(true);
                    after.invoke(target);
                } catch (IllegalAccessException e) {
                    throw new WSchedulerExecutingException(e);
                } catch (InvocationTargetException e) {
                    throw new WSchedulerExecutingException(e);
                }
            }
        }
    }

    @Override
    public void execute(ExecuteParameter parameter) throws WSchedulerExecutingException{
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

    public Method getExecute() {
        return execute;
    }

    public Method getBefore() {
        return before;
    }

    public Method getAfter() {
        return after;
    }

    public Method getInit() {
        return init;
    }

    public Method getDestroy() {
        return destroy;
    }
}
