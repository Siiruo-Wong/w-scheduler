package com.siiruo.wscheduler.core.util;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Created by siiruo wong on 2019/9/13.
 */
public final class StringUtil {

    private final static Pattern EMAIL_PATTERN=Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    private StringUtil() {
    }


    public static String lastAfterSubString(String str, String pattern){
        int lastIndex = str.lastIndexOf(pattern);
        if(lastIndex<0){
            return "";
        }
        return str.substring(lastIndex+1,str.length());
    }

    public static String lastBeforeSubString(String str,String pattern){
        int lastIndex = str.lastIndexOf(pattern);
        if(lastIndex<0){
            return "";
        }
        return str.substring(0,lastIndex);
    }


    public static boolean isEmpty(String value){
        return value==null||value.isEmpty();
    }

    public static boolean isNoBlank(String value){
       return !isBlank(value);
    }

    public static boolean isBlank(String value){
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static  <T> Set<T> split(String value, String pattern, Function<String,T> func){
        String[] splits = value.split(pattern);
        Set<T> set=new HashSet<>();
        for (String split : splits) {
            set.add(func.apply(split));
        }
        return set;
    }

    public static boolean isEmail(String value){
        return EMAIL_PATTERN.matcher(value).matches();
    }

    public static boolean isEquals(String v1,String v2){
        return v1 == null ? v2 == null : v1.equals(v2);
    }
}
