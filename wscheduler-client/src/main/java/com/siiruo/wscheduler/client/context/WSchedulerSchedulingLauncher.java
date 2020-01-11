package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.core.bean.LifecycleExecutor;
import com.siiruo.wscheduler.core.bean.ThreadLauncher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;

/**
 * Created by siiruo wong on 2019/12/29.
 */
public class WSchedulerSchedulingLauncher implements SmartLifecycle,DisposableBean {
    public static final ThreadGroup THREAD_GROUP =new ThreadGroup("WSchedulerThreadGroup");
    private LifecycleExecutor executor;
    private ThreadLauncher executeWorker;
    private volatile boolean status;
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
        if (!isRunning()) {
            status(true);
            this.executor=new ClientExecutor();
            this.executeWorker=new ThreadLauncher(WSchedulerSchedulingLauncher.THREAD_GROUP,"executeWorker",new ExecutionWorker(this.executor));
            this.executor.init();
            this.executeWorker.start();
        }
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
        if (isRunning()) {
            status(false);
            this.executeWorker.stop();
            this.executor.destroy();
        }
    }

    @Override
    public boolean isRunning() {
        return status;
    }

    private void status(boolean status){
        this.status=status;
    }
}
