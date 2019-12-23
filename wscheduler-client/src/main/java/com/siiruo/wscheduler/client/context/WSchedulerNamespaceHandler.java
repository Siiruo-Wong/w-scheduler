package com.siiruo.wscheduler.client.context;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("auto-detect", new WSchedulerBeanDefinitionParser());
    }
}
