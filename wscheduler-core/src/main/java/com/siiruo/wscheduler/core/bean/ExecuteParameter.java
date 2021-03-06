package com.siiruo.wscheduler.core.bean;

import com.siiruo.wscheduler.core.util.DateUtil;
import com.siiruo.wscheduler.core.util.JsonUtil;
import com.siiruo.wscheduler.core.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siiruo wong on 2019/12/25.
 */
public class ExecuteParameter implements Serializable {
    private String executeId;
    private String executorName;
    private Map<String,String> params=new ConcurrentHashMap<>();
    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecuteId() {
        return executeId;
    }

    public void setExecuteId(String executeId) {
        this.executeId = executeId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String paramName){
        return this.params.get(paramName);
    }

    public Boolean getBoolean(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?"true".equalsIgnoreCase(paramValue):null;
    }

    public Character getChar(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?paramValue.charAt(0):null;
    }

    public Byte getByte(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Byte.valueOf(paramValue):null;
    }
    public Short getShort(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Short.valueOf(paramValue):null;
    }

    public Integer getInteger(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Integer.valueOf(paramValue):null;
    }

    public Long getLong(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Long.valueOf(paramValue):null;
    }

    public Double getDouble(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Double.valueOf(paramValue):null;
    }

    public Float getFloat(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?Float.valueOf(paramValue):null;
    }

    public BigDecimal getBigDecimal(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)?new BigDecimal(paramValue):null;
    }

    public Date getDate(String paramName){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)? DateUtil.parse(paramValue):null;
    }
    public Date getDate(String paramName,String format){
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)? DateUtil.parse(paramValue,format):null;
    }

    public <T> T getBean(String paramName, Class<T> clazz){
        if (clazz==null) {
            return null;
        }
        if(clazz==Boolean.class){
            return (T)getBoolean(paramName);
        }else if(clazz==Character.class){
            return (T)getChar(paramName);
        }else if (clazz==String.class) {
            return (T) get(paramName);
        }else if(clazz==Byte.class){
            return (T) getByte(paramName);
        }else if(clazz==Short.class){
            return (T) getShort(paramName);
        }else if(clazz==Integer.class){
            return (T) getInteger(paramName);
        }else if(clazz==Long.class){
            return (T) getLong(paramName);
        }else if(clazz==Double.class){
            return (T) getDouble(paramName);
        }else if(clazz==Float.class){
            return (T) getFloat(paramName);
        }else if(clazz==BigDecimal.class){
            return (T) getBigDecimal(paramName);
        }else if(clazz==Date.class){
            return (T) getDate(paramName);
        }
        String paramValue=get(paramName);
        return StringUtil.isNoBlank(paramValue)? JsonUtil.toBean(paramValue,clazz):null;
    }
}