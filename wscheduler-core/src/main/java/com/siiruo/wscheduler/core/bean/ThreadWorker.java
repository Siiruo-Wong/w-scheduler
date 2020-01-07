package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2019/12/30.
 */
public class ThreadWorker implements Worker {
    private Thread thread;
    private Worker delegate;
    public ThreadWorker(ThreadGroup group, String name, Worker worker) {
        this.delegate=worker;
        this.thread=new Thread(group,()->this.delegate.work(),name);
        this.thread.setDaemon(true);
    }

    @Override
    public void work() {
        this.thread.start();
    }

    @Override
    public void onStart() {}

    @Override
    public void onStop() {
        if (thread!=null && thread.isAlive()) {
            thread.interrupt();
        }
       this.delegate.onStop();
    }
}
