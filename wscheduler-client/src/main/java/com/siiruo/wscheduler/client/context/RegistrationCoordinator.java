package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.business.Registrar;
import com.siiruo.wscheduler.core.bean.Worker;

/**
 * Created by siiruo wong on 2020/1/8.
 */
public class RegistrationCoordinator implements Worker{
    private Registrar registrar;
    private volatile boolean interrupted;
    @Override
    public void work() {
        for (;;){
            if (interrupted) {
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        //Thread.currentThread().isInterrupted()=true
        interrupted=true;
    }

}
