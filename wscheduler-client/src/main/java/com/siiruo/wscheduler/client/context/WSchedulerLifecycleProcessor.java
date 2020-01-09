package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.core.bean.Launcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class WSchedulerLifecycleProcessor implements SmartLifecycle,DisposableBean {
    private volatile boolean status;
    private Launcher schedulingLauncher =new SchedulingLauncher();
    /**
     * be invoked when container is stopping,but actually after {@link #stop()}
     * aim to destroy itself
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        //nothing todo for itself
    }

    /**
     * be invoked when container was loaded completely(all beans were loaded,instantiated,initialized)
     */
    @Override
    public void start() {
        status(true);
        schedulingLauncher.start();
    }

    /**
     * be invoked when container is stopping,and  before {@link #destroy()}
     * aim to destroy {@link ClientExecutor} and {@link SingleExecutor}
     * @see ClientExecutor
     * @see SingleExecutor
     * @throws Exception
     */
    @Override
    public void stop() {
        status(false);
        schedulingLauncher.stop();
    }

    @Override
    public boolean isRunning() {
        return status;
    }

    private void status(boolean status){
        this.status=status;
    }
}
