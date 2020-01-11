package com.siiruo.wscheduler.core.bean;


import java.io.Serializable;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public class ExecutorInfo implements Serializable {
    private String target;
    private String name;
    private String execute;
    private String before;
    private String after;
    private String init;
    private String destroy;

    public ExecutorInfo() {
    }

    public ExecutorInfo(String target, String name, String execute, String before, String after, String init, String destroy) {
        this.target = target;
        this.name = name;
        this.execute = execute;
        this.before = before;
        this.after = after;
        this.init = init;
        this.destroy = destroy;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getDestroy() {
        return destroy;
    }

    public void setDestroy(String destroy) {
        this.destroy = destroy;
    }
}
