package com.siiruo.wscheduler.client.util;

import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.client.config.WSchedulerClient;
import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.exception.WSchedulerLoadingException;
import com.siiruo.wscheduler.core.util.PropertyUtil;
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
    public static BeanDefinition registerWSchedulerAutoDetectorIfNecessary(Class<?> clazz, BeanDefinitionRegistry registry,Object source) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry.containsBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_DETECTOR_BEAN_NAME)) {
            return null;
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
        beanDefinition.setSource(source);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(WSchedulerConstantType.W_SCHEDULER_AUTO_DETECTOR_BEAN_NAME, beanDefinition);
        return beanDefinition;
    }

    public static void buildClient() throws WSchedulerLoadingException{
        Properties properties;
        try {
            properties= PropertyUtil.getProperties(WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES);
        } catch (IOException e) {
            LOGGER.error("fail to load {} file.",WSchedulerConstantType.W_SCHEDULER_CLIENT_APP_INFO_PROPERTIES);
            throw new WSchedulerLoadingException("could not load wscheduler client configuration file.\r\nplease add app.properties file in classpath.",e);
        }

        WSchedulerClient client=new WSchedulerClient();
        client.setClientIp(properties.getProperty(WSchedulerConstantType.CLIENT_IP_PROPERTY_NAME));
        client.setClientPort(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_PORT_PROPERTY_NAME)));
        client.setAppId(Long.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_APP_ID_PROPERTY_NAME)));
        client.setAppName(properties.getProperty(WSchedulerConstantType.CLIENT_APP_NAME_PROPERTY_NAME));
        client.setAppDesc(properties.getProperty(WSchedulerConstantType.CLIENT_APP_DESC_PROPERTY_NAME));
        client.setServerUrl(properties.getProperty(WSchedulerConstantType.CLIENT_SERVER_URL_PROPERTY_NAME));
        client.setServerPort(Integer.valueOf(properties.getProperty(WSchedulerConstantType.CLIENT_PORT_PROPERTY_NAME)));

        WSchedulerContextHolder.setClient(client);
    }

}
