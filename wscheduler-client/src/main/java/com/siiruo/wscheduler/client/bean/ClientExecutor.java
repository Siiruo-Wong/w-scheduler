package com.siiruo.wscheduler.client.bean;

import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.ExecutorParameter;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class ClientExecutor extends AbstractExecutor{
    @Override
    public void execute(ExecutorParameter parameter) {
        SingleExecutor executor = WSchedulerContextHolder.getExecutor(parameter.getExecutorName());
        if (executor!=null) {
            executor.execute(parameter);
        }
    }

    @Override
    public void init() {
        Map<String, SingleExecutor> executors = WSchedulerContextHolder.getExecutors();
        Iterator<Map.Entry<String, SingleExecutor>> it = executors.entrySet().iterator();
        for(;it.hasNext();){
            it.next().getValue().init();
        }
    }

    @Override
    public void destroy() {
        Map<String, SingleExecutor> executors = WSchedulerContextHolder.getExecutors();
        Iterator<Map.Entry<String, SingleExecutor>> it = executors.entrySet().iterator();
        for(;it.hasNext();){
            it.next().getValue().destroy();
        }
    }
}
