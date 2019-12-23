package com.siiruo.wscheduler.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public final class PropertyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
    private static ConcurrentHashMap<String,Properties> properties=new ConcurrentHashMap<>();
    public static Properties getProperties(String path) throws IOException{
        if (properties.contains(path)) {
            return properties.get(path);
        }
        Properties property;
        try {
            (property=new Properties()).load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(path),"utf-8"));
            properties.put(path,property);
            return property;
        } catch (IOException e) {
            LOGGER.error("IOException occurs when loading {} file.",path,e);
            throw e;
        }
    }

    public static String getProperty(String path,String key) throws IOException{
        return getProperty(path,key,null);
    }

    public static String getProperty(String path, String key,String defaultValue) throws IOException{
        Properties property=getProperties(path);
        return property==null?defaultValue:property.getProperty(key);
    }
}
