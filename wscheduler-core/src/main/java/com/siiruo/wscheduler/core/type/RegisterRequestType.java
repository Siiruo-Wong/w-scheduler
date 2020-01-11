package com.siiruo.wscheduler.core.type;

import com.siiruo.wscheduler.core.bean.ExecutorInfo;

import java.util.List;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public class RegisterRequestType extends RequestType{
    private String clientIp;
    private Integer clientPort;
    private Long appId;
    private String appName;
    private String appDesc;
    private List<ExecutorInfo> executors;

    public RegisterRequestType() {
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public List<ExecutorInfo> getExecutors() {
        return executors;
    }

    public void setExecutors(List<ExecutorInfo> executors) {
        this.executors = executors;
    }
}
