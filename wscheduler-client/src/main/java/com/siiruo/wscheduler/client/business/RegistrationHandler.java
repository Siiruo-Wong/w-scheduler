package com.siiruo.wscheduler.client.business;

import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.ApiConstantType;
import com.siiruo.wscheduler.core.type.RegisterRequestType;
import com.siiruo.wscheduler.core.type.RegisterResponseType;
import com.siiruo.wscheduler.core.type.ResponseCodeType;
import com.siiruo.wscheduler.core.type.ResultType;
import com.siiruo.wscheduler.core.util.HttpClientUtil;
import com.siiruo.wscheduler.core.util.URLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by siiruo wong on 2020/1/8.
 */
public class RegistrationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandler.class);
    private WSchedulerClientConfig clientConfig= WSchedulerContextHolder.getClientConfig();
    public RegisterResponseType register(RegisterRequestType parameter){
        String url= URLUtil.buildURL(clientConfig.getServerUrl(),clientConfig.getServerPort(), ApiConstantType.EXECUTOR_REGISTER_PATH_IN_SERVER);
        RegisterResponseType response=null;
        try {
            response= HttpClientUtil.doPost(url, parameter, RegisterResponseType.class);
        } catch (Exception e) {
            LOGGER.error("an Exception occurs when registering application and executor info.",e);
        }
        if (response==null) {
            response=new RegisterResponseType(new ResultType(ResponseCodeType.UN_KNOWN));
        }
        return response;
    }
}
