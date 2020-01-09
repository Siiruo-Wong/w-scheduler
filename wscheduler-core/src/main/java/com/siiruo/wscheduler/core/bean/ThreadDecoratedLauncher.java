package com.siiruo.wscheduler.core.bean;

/**
 * Created by siiruo wong on 2019/12/30.
 */
public class ThreadDecoratedLauncher implements Launcher {
    private Thread thread;
    private Worker delegate;
    public ThreadDecoratedLauncher(ThreadGroup group, String name, Worker worker) {
        this.delegate=worker;
        this.thread=new Thread(group,()->this.delegate.work(),name);
        this.thread.setDaemon(true);
    }

    @Override
    public void start() {
        this.thread.start();
    }

    @Override
    public void stop() {
        if (thread!=null && thread.isAlive()) {
            thread.interrupt();
        }
        this.delegate.interrupt();
    }

    @Override
    public boolean status() {
        return true;
    }
}
