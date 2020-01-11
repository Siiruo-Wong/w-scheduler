package com.siiruo.wscheduler.core.type;

/**
 * Created by siiruo wong on 2020/1/11.
 */
public class DefaultResponseType extends ResponseType{
    public DefaultResponseType() {
        this.resultType=new ResultType(ResponseCodeType.SUCCESS);
    }

    public DefaultResponseType(ResultType resultType) {
        super(resultType);
    }
}
