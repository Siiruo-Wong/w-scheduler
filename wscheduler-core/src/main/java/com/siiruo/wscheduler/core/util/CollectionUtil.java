package com.siiruo.wscheduler.core.util;

import java.util.Collection;

/**
 * Created by siiruo wong on 2020/1/16.
 */
public final class CollectionUtil {
    private CollectionUtil(){}
    public static boolean isEmpty(Collection<?> eles){
        return eles == null || eles.isEmpty();
    }
    public static boolean isNotEmpty(Collection<?> eles){
        return !isEmpty(eles);
    }
}
