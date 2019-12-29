package com.siiruo.wscheduler.core.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siiruo wong on 2019/12/25.
 */
public final class ClassUtil {
    private ClassUtil(){}

    /**
     * 是否包含特定类型的参数
     * @param method
     * @param paramType
     * @return
     */
    public static boolean isContainsParamType(Method method, Class paramType){
        if (method==null||paramType==null) {
            return false;
        }
        for (Class<?> type : method.getParameterTypes()) {
            if (type.equals(paramType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnlyContainsParamType(Method method, Class paramType){
        if (method==null||method.getParameterTypes().length!=1||paramType==null) {
            return false;
        }
        return paramType.equals(method.getParameterTypes()[0]);
    }

}
