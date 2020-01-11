package com.siiruo.wscheduler.client.business;

import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.ApiConstantType;
import com.siiruo.wscheduler.core.type.RegisterRequestType;
import com.siiruo.wscheduler.core.exception.WSchedulerRemoteException;
import com.siiruo.wscheduler.core.type.RegisterResponseType;
import com.siiruo.wscheduler.core.util.URLUtil;

/**
 * Created by siiruo wong on 2020/1/8.
 */
public class RegistrationHandler {
    private WSchedulerClientConfig clientConfig= WSchedulerContextHolder.getClientConfig();
    public RegisterResponseType register(RegisterRequestType parameter) throws WSchedulerRemoteException {
        String url= URLUtil.buildURL(clientConfig.getServerUrl(),clientConfig.getClientPort(), ApiConstantType.EXECUTOR_REGISTER_PATH_IN_CONSOLE);
        return new RegisterResponseType();
    }
}
