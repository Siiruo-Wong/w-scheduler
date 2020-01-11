package com.siiruo.wscheduler.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siiruo wong on 2020/1/11.
 */
public final class DateUtil {

    public static final String YYYY_MM_DD_HH_mm_ss_SSS="yyyy-MM-dd HH:mm:ss.SSS";
    private DateUtil(){}

    public static Date parse(String date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {

        }
        return null;
    }

    public static Date parse(String date){
        return parse(date,YYYY_MM_DD_HH_mm_ss_SSS);
    }

}
