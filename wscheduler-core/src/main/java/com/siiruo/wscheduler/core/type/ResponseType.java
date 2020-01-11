package com.siiruo.wscheduler.core.type;

import java.io.Serializable;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public abstract class ResponseType implements Serializable {
    public ResultType resultType;

    public ResponseType() {
    }

    public ResponseType(ResultType resultType) {
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }
}
