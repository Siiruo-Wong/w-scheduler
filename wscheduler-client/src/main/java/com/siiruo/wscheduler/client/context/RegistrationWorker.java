package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.business.RegistrationHandler;
import com.siiruo.wscheduler.core.type.RegisterRequestType;
import com.siiruo.wscheduler.core.bean.Worker;
import com.siiruo.wscheduler.core.type.RegisterResponseType;
import com.siiruo.wscheduler.core.type.ResponseCodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by siiruo wong on 2020/1/8.
 */
public class RegistrationWorker implements Worker{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationWorker.class);
    private final RegistrationHandler registrationHandler=new RegistrationHandler();
    private volatile boolean interrupted;
    @Override
    public void work() {
        RegisterRequestType registerParameter=WSchedulerContextHolder.getRegisterInfo();
        RegisterResponseType response;
        for (;;){
            try {
                response = this.registrationHandler.register(registerParameter);
                if ((ResponseCodeType.SUCCESS.code==response.getResult().getCode())||this.interrupted) {
                    break;
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                LOGGER.warn("The {} service has been terminated.",RegistrationWorker.class.getName(), e);
                break;
            }catch (Exception e){
                LOGGER.error("The {} service catches an exception but continues to attempt to register.",RegistrationWorker.class.getName(),e);
            }
        }
    }

    @Override
    public void interrupt() {
        //Thread.currentThread().isInterrupted()=true
        LOGGER.info("The {} service has been interrupted.",RegistrationWorker.class.getName());
        this.interrupted=true;
    }



}
