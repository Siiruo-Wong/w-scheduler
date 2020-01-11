package com.siiruo.wscheduler.core.type;

import java.io.Serializable;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public class ResultType  implements Serializable {
    private int code;
    private String msg;

    public ResultType() {
    }

    public ResultType(ResponseCodeType codeType) {
        this(codeType.code,codeType.msg);
    }


    public ResultType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
