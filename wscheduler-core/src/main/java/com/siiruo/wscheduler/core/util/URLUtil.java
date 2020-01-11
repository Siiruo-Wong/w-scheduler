package com.siiruo.wscheduler.core.util;

import org.apache.http.util.Asserts;

/**
 * Created by siiruo wong on 2020/1/10.
 */
public final class URLUtil {
    private URLUtil(){}

    public static String buildURL(String url,Integer port,String path){
        Asserts.notBlank(url,"url");
        String sPort=port==null||port<=0?"":(":"+port);
        String sPath=StringUtil.isBlank(path)?"":path;
        return url+sPort+sPath;
    }
}
