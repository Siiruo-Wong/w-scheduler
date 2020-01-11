package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.client.business.RegistrationHandler;
import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.core.bean.ExecutorInfo;
import com.siiruo.wscheduler.core.type.RegisterRequestType;
import com.siiruo.wscheduler.core.bean.Worker;
import com.siiruo.wscheduler.core.type.RegisterResponseType;
import com.siiruo.wscheduler.core.type.ResponseCodeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by siiruo wong on 2020/1/8.
 */
public class RegistrationWorker implements Worker{
    private final RegistrationHandler registrationHandler=new RegistrationHandler();
    private RegisterRequestType registerParameter;
    private volatile boolean interrupted;
    @Override
    public void work() {
        registerParameter=WSchedulerContextHolder.getRegisterInfo();
        RegisterResponseType response;
        for (;;){
            response = registrationHandler.register(registerParameter);
            if ((ResponseCodeType.SUCCESS.code==response.getResultType().getCode())||interrupted) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
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
