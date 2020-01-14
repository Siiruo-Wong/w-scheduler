package com.siiruo.wscheduler.core.type;

import com.siiruo.wscheduler.core.bean.ExecuteParameter;

/**
 * Created by siiruo wong on 2020/1/14.
 */
public class ExecuteRequestType extends RequestType {
    private ExecuteParameter parameter;

    public ExecuteRequestType() {
    }

    public ExecuteParameter getParameter() {
        return parameter;
    }

    public void setParameter(ExecuteParameter parameter) {
        this.parameter = parameter;
    }
}
