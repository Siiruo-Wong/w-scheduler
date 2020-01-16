package com.siiruo.wscheduler.client.bean;

import com.siiruo.wscheduler.client.context.MemoryNoticeWorker;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.LifecycleExecutor;
import com.siiruo.wscheduler.core.bean.ExecuteParameter;
import com.siiruo.wscheduler.core.exception.WSchedulerExecutingException;
import com.siiruo.wscheduler.core.bean.NoticeParameter;
import com.siiruo.wscheduler.core.util.ExceptionUtil;

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
            NoticeParameter noticeParameter = initNoticeParam(parameter);
            try {
                executor.execute(parameter);
                noticeParameter.setEndTime(System.currentTimeMillis());
            } catch (WSchedulerExecutingException e) {
                noticeParameter.setEndTime(System.currentTimeMillis());
                noticeParameter.setDescription("failure");
                noticeParameter.setThrowable(ExceptionUtil.convert(e));
            }
            MemoryNoticeWorker.addNotice(noticeParameter);
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

    private NoticeParameter initNoticeParam(ExecuteParameter parameter){
        NoticeParameter noticeParameter =new NoticeParameter();
        noticeParameter.setExecuteId(parameter.getExecuteId());
        noticeParameter.setExecutorName(parameter.getExecutorName());
        noticeParameter.setNoticeCount(1);
        noticeParameter.setDescription("success");
        noticeParameter.setThrowable("");
        noticeParameter.setStartTime(System.currentTimeMillis());
        return noticeParameter;
    }
}
