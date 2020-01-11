package com.siiruo.wscheduler.client.bean;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public abstract class WSchedulerConstantType {
    public static final String W_SCHEDULER_AUTO_DETECTOR_BEAN_NAME = "com.siiruo.wscheduler.client.context.WSchedulerAutoDetector";
    public static final String W_SCHEDULER_AUTO_LIFECYCLE_PROCESSOR_BEAN_NAME = "com.siiruo.wscheduler.client.context.WSchedulerSchedulingLauncher";
    public static final String W_SCHEDULER_CLIENT_CONTEXT_PATH="/wscheduler";
    public static final String W_SCHEDULER_CLIENT_BEAN_NAME="wscheduler-client";
    public static final String W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES="app.properties";
    public static final String CLIENT_IP_PROPERTY_NAME="wscheduler.client.ip";
    public static final String CLIENT_PORT_PROPERTY_NAME="wscheduler.client.port";
    public static final String CLIENT_APP_ID_PROPERTY_NAME="wscheduler.client.app.id";
    public static final String CLIENT_APP_NAME_PROPERTY_NAME="wscheduler.client.app.name";
    public static final String CLIENT_APP_DESC_PROPERTY_NAME="wscheduler.client.app.desc";
    public static final String CLIENT_SERVER_URL_PROPERTY_NAME="wscheduler.client.server.url";
    public static final String CLIENT_SERVER_PORT_PROPERTY_NAME="wscheduler.client.server.port";
    public static final String DEFAULT_EXECUTOR_EXECUTE_METHOD_NAME="execute";
    public static final String DEFAULT_EXECUTOR_BEFORE_METHOD_NAME="before";
    public static final String DEFAULT_EXECUTOR_AFTER_METHOD_NAME ="after";
    public static final String DEFAULT_EXECUTOR_INIT_METHOD_NAME="init";
    public static final String DEFAULT_EXECUTOR_DESTROY_METHOD_NAME="destroy";
    public static final int BEAT_TIME_PERIOD = 30;
}
