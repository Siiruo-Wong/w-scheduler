package com.siiruo.wscheduler.client.business;

import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.ApiConstantType;
import com.siiruo.wscheduler.core.bean.NoticeParameter;
import com.siiruo.wscheduler.core.type.*;
import com.siiruo.wscheduler.core.util.CollectionUtil;
import com.siiruo.wscheduler.core.util.HttpClientUtil;
import com.siiruo.wscheduler.core.util.URLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by siiruo wong on 2020/1/16.
 */
public  class NoticeHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeHandler.class);
    private WSchedulerClientConfig clientConfig= WSchedulerContextHolder.getClientConfig();
    public NoticeResponseType notice(List<NoticeParameter> notices){
        if (CollectionUtil.isEmpty(notices)) {
            return new NoticeResponseType(new ResultType(ResponseCodeType.SUCCESS));
        }
        String url= URLUtil.buildURL(clientConfig.getServerUrl(),clientConfig.getClientPort(), ApiConstantType.EXECUTOR_NOTICE_PATH_IN_SERVER);
        NoticeResponseType response=null;
        try {
            NoticeRequestType request=new NoticeRequestType();
            request.setNotices(notices);
            response= HttpClientUtil.doPost(url, request, NoticeResponseType.class);
        } catch (Exception e) {
            LOGGER.error("an Exception occurs when registering application and executor info.",e);
        }
        if (response==null) {
            response=new NoticeResponseType(new ResultType(ResponseCodeType.UN_KNOWN));
        }
        return response;
    }
}
