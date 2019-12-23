package com.siiruo.wscheduler.client.context;


import com.siiruo.wscheduler.client.config.WSchedulerClient;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerContextHolder{
    private static WSchedulerClient client;

    public static WSchedulerClient getClient() {
        return client;
    }

    public static void setClient(WSchedulerClient client) {
        if (WSchedulerContextHolder.client!=null) {
            return;
        }
        WSchedulerContextHolder.client = client;
    }
}
