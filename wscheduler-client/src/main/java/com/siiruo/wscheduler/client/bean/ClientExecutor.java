package com.siiruo.wscheduler.client.bean;

import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.LifecycleExecutor;
import com.siiruo.wscheduler.core.bean.ExecuteParameter;
import com.siiruo.wscheduler.core.exception.WSchedulerExecutingException;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class ClientExecutor implements LifecycleExecutor {
    @Override
    public void execute(ExecuteParameter parameter) throws WSchedulerExecutingException {
        SingleExecutor executor = WSchedulerContextHolder.getExecutor(parameter.getExecutorName());
        if (executor!=null) {
            try {
                executor.execute(parameter);
            } catch (WSchedulerExecutingException e) {

            }
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
