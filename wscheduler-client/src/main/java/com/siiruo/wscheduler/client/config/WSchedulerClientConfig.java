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
    private String logPath;
    private Integer coreThreads;
    private Integer maxThreads;
    private Long keepAliveTime;
    private Integer queueCapacity;


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

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Integer getCoreThreads() {
        return coreThreads;
    }

    public void setCoreThreads(Integer coreThreads) {
        this.coreThreads = coreThreads;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
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
                ", logPath='" + logPath + '\'' +
                ", coreThreads=" + coreThreads +
                ", maxThreads=" + maxThreads +
                ", keepAliveTime=" + keepAliveTime +
                ", queueCapacity=" + queueCapacity +
                '}';
    }
}
