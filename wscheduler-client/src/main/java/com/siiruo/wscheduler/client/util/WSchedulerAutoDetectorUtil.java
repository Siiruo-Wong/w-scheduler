package com.siiruo.wscheduler.client.util;

import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.client.context.WSchedulerAutoDetector;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.client.context.WSchedulerSchedulingLauncher;
import com.siiruo.wscheduler.core.exception.WSchedulerLoadingException;
import com.siiruo.wscheduler.core.util.NetUtil;
import com.siiruo.wscheduler.core.util.PropertyUtil;
import com.siiruo.wscheduler.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerAutoDetectorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WSchedulerAutoDetectorUtil.class);

    public static BeanDefinition registerWSchedulerAutoDetectorIfNecessary(BeanDefinitionRegistry registry,Object source) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry.containsBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_DETECTOR_BEAN_NAME)) {
            return null;
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition(WSchedulerAutoDetector.class);
        beanDefinition.setSource(source);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_DETECTOR_BEAN_NAME, beanDefinition);
        return beanDefinition;
    }

    public static BeanDefinition registerWSchedulerLifecycleProcessorIfNecessary(BeanDefinitionRegistry registry,Object source) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry.containsBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_LIFECYCLE_PROCESSOR_BEAN_NAME)) {
            return null;
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition(WSchedulerSchedulingLauncher.class);
        beanDefinition.setSource(source);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_LIFECYCLE_PROCESSOR_BEAN_NAME, beanDefinition);
        return beanDefinition;
    }

    public static void buildClientIfNecessary() throws WSchedulerLoadingException{
        Properties properties;
        try {
            properties= PropertyUtil.getProperties(WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES);
        } catch (IOException e) {
            LOGGER.error("fail to load {} file.",WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES,e);
            throw new WSchedulerLoadingException("could not load w-scheduler client configuration file." +
                    "\r\nplease add "+WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES+" file in classpath.",e);
        }

        WSchedulerClientConfig clientConfig=new WSchedulerClientConfig();

        //ip
        clientConfig.setClientIp(properties.getProperty(WSchedulerConstantType.CLIENT_IP_PROPERTY_NAME));
        if (StringUtil.isBlank(clientConfig.getClientIp())) {
            clientConfig.setClientIp(NetUtil.getInetAddress().getHostAddress());
        }

        //port
        clientConfig.setClientPort(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_PORT_PROPERTY_NAME)));
        if(clientConfig.getClientPort()==null||clientConfig.getClientPort()<=0){
            clientConfig.setClientPort(7788);//default
        }

        //app
        clientConfig.setAppId(Long.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_APP_ID_PROPERTY_NAME)));
        clientConfig.setAppName(properties.getProperty(WSchedulerConstantType.CLIENT_APP_NAME_PROPERTY_NAME));
        if (clientConfig.getAppId()==null||clientConfig.getAppId()<=0||StringUtil.isBlank(clientConfig.getAppName())) {
            throw new WSchedulerLoadingException("could not load w-scheduler client." +
                    "\r\nplease check "+WSchedulerConstantType.CLIENT_APP_ID_PROPERTY_NAME+" or "+WSchedulerConstantType.CLIENT_APP_NAME_PROPERTY_NAME+" in "+
                    WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES+" file in classpath.");
        }
        clientConfig.setAppDesc(properties.getProperty(WSchedulerConstantType.CLIENT_APP_DESC_PROPERTY_NAME));

        //server
        clientConfig.setServerUrl(properties.getProperty(WSchedulerConstantType.CLIENT_SERVER_URL_PROPERTY_NAME));
        if (StringUtil.isBlank(clientConfig.getServerUrl())) {
            throw new WSchedulerLoadingException("could not load w-scheduler client." +
                    "\r\nplease check "+WSchedulerConstantType.CLIENT_SERVER_URL_PROPERTY_NAME+" in "+
                    WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES+" file in classpath.");
        }
        clientConfig.setServerPort(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_SERVER_PORT_PROPERTY_NAME)));

        //log
        clientConfig.setLogPath(properties.getProperty(WSchedulerConstantType.CLIENT_LOG_PATH_PROPERTY_NAME));
        if (StringUtil.isBlank(clientConfig.getLogPath())) {
            clientConfig.setLogPath("/data/log/wscheduler");
        }
        //thread
        clientConfig.setCoreThreads(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_THREADPOOL_CORE_SIZE_PROPERTY_NAME)));
        if(clientConfig.getCoreThreads()==null||clientConfig.getCoreThreads()<=0){
            clientConfig.setCoreThreads(10);//default
        }
        clientConfig.setMaxThreads(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_THREADPOOL_MAX_SIZE_PROPERTY_NAME)));
        if(clientConfig.getMaxThreads()==null||clientConfig.getMaxThreads()<=0){
            clientConfig.setMaxThreads(100);//default
        }

        clientConfig.setKeepAliveTime(Long.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_THREADPOOL_KEEP_ALIVE_TIME_PROPERTY_NAME)));
        if(clientConfig.getKeepAliveTime()==null||clientConfig.getKeepAliveTime()<=0){
            clientConfig.setKeepAliveTime(60000L);//default
        }
        clientConfig.setQueueCapacity(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_THREADPOOL_QUEUE_CAPACITY_PROPERTY_NAME)));
        if(clientConfig.getQueueCapacity()==null||clientConfig.getQueueCapacity()<=0){
            clientConfig.setQueueCapacity(1000);//default
        }

        WSchedulerContextHolder.setClientConfig(clientConfig);
    }

}
