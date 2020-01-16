package com.siiruo.wscheduler.core.type;

import com.siiruo.wscheduler.core.bean.NoticeParameter;

import java.util.List;

/**
 * Created by siiruo wong on 2020/1/16.
 */
public class NoticeRequestType extends RequestType {
    private List<NoticeParameter> notices;

    public List<NoticeParameter> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticeParameter> notices) {
        this.notices = notices;
    }
}
