package com.siiruo.wscheduler.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by siiruo wong on 2020/1/16.
 */
public final class ExceptionUtil {
    private ExceptionUtil(){}

    public static String convert(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "ExceptionUtil#convert";
        }finally {
            try {
                sw.close();
            } catch (IOException e) {
               //ignore
            }
            pw.close();
        }
    }


}
