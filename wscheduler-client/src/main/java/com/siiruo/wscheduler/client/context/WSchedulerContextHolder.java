package com.siiruo.wscheduler.client.context;


import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.client.config.WSchedulerClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerContextHolder{
    private static WSchedulerClient client;
    private static Map<String,SingleExecutor> executors=new ConcurrentHashMap<>();

    public static WSchedulerClient getClient() {
        return WSchedulerContextHolder.client;
    }

    public static synchronized void setClient(WSchedulerClient client) {
        if (WSchedulerContextHolder.client!=null) {
            return;
        }
        WSchedulerContextHolder.client = client;
    }

    public static Map<String,SingleExecutor> getExecutors() {
        return WSchedulerContextHolder.executors;
    }
    public static SingleExecutor getExecutor(String name) {
        return WSchedulerContextHolder.executors.get(name);
    }

    public static void addExecutor(SingleExecutor executor) {
        WSchedulerContextHolder.executors.put(executor.getName(),executor);
    }
}
