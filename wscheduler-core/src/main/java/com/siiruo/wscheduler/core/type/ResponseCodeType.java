package com.siiruo.wscheduler.core.type;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public enum ResponseCodeType {
    UN_KNOWN(-1,"unknown"),
    SUCCESS(0,"success");

    public final int code;
    public final String msg;
    ResponseCodeType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseCodeType valueOf(int code){
        ResponseCodeType[] values = values();
        for (ResponseCodeType value : values) {
            if (value.code==code) {
                return value;
            }
        }
        return UN_KNOWN;
    }

}
