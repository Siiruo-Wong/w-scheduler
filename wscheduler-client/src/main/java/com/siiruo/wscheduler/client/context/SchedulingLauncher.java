package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.Launcher;
import com.siiruo.wscheduler.core.bean.ThreadDecoratedLauncher;

/**
 * Created by siiruo wong on 2019/12/30.
 */
public class SchedulingLauncher implements Launcher {
    public static final ThreadGroup THREAD_GROUP =new ThreadGroup("WSchedulerThreadGroup");
    private AbstractExecutor executor;
    private ThreadDecoratedLauncher executeWorker;
    private volatile boolean working;
    @Override
    public void start() {
        if (!status()) {
            setWorking(true);
            this.executor=new ClientExecutor();
            this.executeWorker=new ThreadDecoratedLauncher(SchedulingLauncher.THREAD_GROUP,"executeWorker",new ExecutionCoordinator(this.executor));
            this.executor.init();
            this.executeWorker.start();
        }
    }

    @Override
    public void stop() {
        if (status()) {
            setWorking(false);
            this.executeWorker.stop();
            this.executor.destroy();
        }
    }

    @Override
    public boolean status() {
        return this.working;
    }

    private void setWorking(boolean working) {
        this.working = working;
    }
}
