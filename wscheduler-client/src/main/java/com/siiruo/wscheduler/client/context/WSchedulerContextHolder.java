package com.siiruo.wscheduler.client.context;


import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.core.bean.ExecutorInfo;
import com.siiruo.wscheduler.core.exception.WSchedulerExecutorConflictException;
import com.siiruo.wscheduler.core.type.RegisterRequestType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerContextHolder{
    private static WSchedulerClientConfig clientConfig;
    private static Map<String,SingleExecutor> executors=new ConcurrentHashMap<>();

    public static WSchedulerClientConfig getClientConfig() {
        return WSchedulerContextHolder.clientConfig;
    }

    public static synchronized void setClientConfig(WSchedulerClientConfig clientConfig) {
        if (WSchedulerContextHolder.clientConfig !=null) {
            return;
        }
        WSchedulerContextHolder.clientConfig = clientConfig;
    }

    public static Map<String,SingleExecutor> getExecutors() {
        return WSchedulerContextHolder.executors;
    }
    public static SingleExecutor getExecutor(String name) {
        return WSchedulerContextHolder.executors.get(name);
    }

    public static void addExecutor(SingleExecutor executor) {
        SingleExecutor oldExecutor = executors.get(executor.getName());
        if (oldExecutor!=null) {
            throw new WSchedulerExecutorConflictException(
                    String.format("the executor named {} in {}",executor.getName(),executor.getClass().getName()),
                    String.format("the executor named {} in {}",oldExecutor.getName(),oldExecutor.getClass().getName()));
        }
        WSchedulerContextHolder.executors.put(executor.getName(),executor);
    }

    public static RegisterRequestType getRegisterInfo(){
        WSchedulerClientConfig clientConfig= WSchedulerContextHolder.getClientConfig();
        RegisterRequestType parameter=new RegisterRequestType();
        parameter.setAppId(clientConfig.getAppId());
        parameter.setAppDesc(clientConfig.getAppDesc());
        parameter.setAppName(clientConfig.getAppName());
        parameter.setClientIp(clientConfig.getClientIp());
        parameter.setClientPort(clientConfig.getClientPort());


        Map<String, SingleExecutor> executors = WSchedulerContextHolder.getExecutors();
        List<ExecutorInfo> executorInfos=new ArrayList<>(executors.size());
        Iterator<Map.Entry<String, SingleExecutor>> it = executors.entrySet().iterator();
        for(;it.hasNext();){
            SingleExecutor value = it.next().getValue();
            ExecutorInfo executorInfo=new ExecutorInfo();
            executorInfo.setName(value.getName());
            executorInfo.setTarget(value.getTarget().getClass().getName());
            executorInfo.setTarget(value.getExecute().getName());
            if (value.getInit()!=null) {
                executorInfo.setInit(value.getInit().getName());
            }
            if (value.getDestroy()!=null) {
                executorInfo.setDestroy(value.getDestroy().getName());
            }
            if (value.getBefore()!=null) {
                executorInfo.setBefore(value.getBefore().getName());
            }
            if (value.getAfter()!=null) {
                executorInfo.setAfter(value.getAfter().getName());
            }
            executorInfos.add(executorInfo);
        }

        parameter.setExecutors(executorInfos);
        return parameter;
    }
}
