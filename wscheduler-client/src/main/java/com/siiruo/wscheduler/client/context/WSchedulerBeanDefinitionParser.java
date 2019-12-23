package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.util.WSchedulerAutoDetectorUtil;
import com.siiruo.wscheduler.core.exception.WSchedulerLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.io.IOException;


/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerBeanDefinitionParser implements BeanDefinitionParser{
    private static final Logger LOGGER = LoggerFactory.getLogger(WSchedulerBeanDefinitionParser.class);
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        WSchedulerAutoDetectorUtil.buildClient();
        WSchedulerAutoDetectorUtil.registerWSchedulerAutoDetectorIfNecessary(
                WSchedulerAutoDetector.class,parserContext.getRegistry(), parserContext.extractSource(element));
        return null;
    }
}
