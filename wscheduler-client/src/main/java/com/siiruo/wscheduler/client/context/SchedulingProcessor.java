package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.client.config.WSchedulerClient;
import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.ThreadWorker;
import com.siiruo.wscheduler.core.bean.Worker;

/**
 * Created by siiruo wong on 2019/12/30.
 */
public class SchedulingProcessor implements Worker {
    private final ThreadGroup threadGroup=new ThreadGroup("WSchedulerThreadGroup");
    private final AbstractExecutor executor=new ClientExecutor();
    private final ThreadWorker executeWorker=new ThreadWorker(threadGroup,"executeWorker",new RpcClientWorker(executor));

    private volatile boolean initialized;

    @Override
    public void work() {
        onStart();
        executeWorker.work();
    }

    @Override
    public void onStart() {
        if (!initialized) {
            initialized=true;
            executor.init();
            executeWorker.onStart();
        }
    }

    @Override
    public void onStop() {
        executeWorker.onStop();
        executor.destroy();
    }
}
