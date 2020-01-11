package com.siiruo.wscheduler.core.bean;

import com.siiruo.wscheduler.core.exception.WSchedulerExecutingException;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public interface Executor {
    void execute(ExecuteParameter parameter) throws WSchedulerExecutingException;
}
