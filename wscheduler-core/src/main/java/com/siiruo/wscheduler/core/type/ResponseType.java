package com.siiruo.wscheduler.core.type;

import java.io.Serializable;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public abstract class ResponseType implements Serializable {
    public ResultType result;

    public ResponseType() {
    }

    public ResponseType(ResultType result) {
        this.result = result;
    }

    public ResultType getResult() {
        return result;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }
}
