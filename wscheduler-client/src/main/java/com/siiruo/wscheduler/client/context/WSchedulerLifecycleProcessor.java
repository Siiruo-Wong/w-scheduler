package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.client.bean.SingleExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class WSchedulerLifecycleProcessor implements SmartLifecycle,DisposableBean {
    private volatile boolean status;
    /**
     * be invoked when container is stopping,but actually after {@link #stop()}
     * aim to destroy itself
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {

    }

    /**
     * be invoked when container was loaded completely(all beans were loaded,instantiated,initialized)
     */
    @Override
    public void start() {
        //todo sth
        status(true);
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
        //todo sth
        status(false);
    }

    @Override
    public boolean isRunning() {
        return status;
    }

    private void status(boolean status){
        this.status=status;
    }
}
