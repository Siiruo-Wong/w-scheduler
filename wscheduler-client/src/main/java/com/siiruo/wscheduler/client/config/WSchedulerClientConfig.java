package com.siiruo.wscheduler.client.config;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerClientConfig {
    private String clientIp;
    private Integer clientPort;
    private Long appId;
    private String appName;
    private String appDesc;
    private String serverUrl;
    private Integer serverPort;


    public WSchedulerClientConfig() {
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

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "WSchedulerClientConfig{" +
                "clientIp='" + clientIp + '\'' +
                ", clientPort=" + clientPort +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                ", appDesc='" + appDesc + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
