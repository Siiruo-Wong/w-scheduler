package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.WSchedulerBannerVersion;
import com.siiruo.wscheduler.client.util.WSchedulerAutoDetectorUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Registers a {@link com.siiruo.wscheduler.client.context.WSchedulerAutoDetector} against the current {@link BeanDefinitionRegistry}
 * as appropriate based on a given @{@link com.siiruo.wscheduler.client.annotation.EnableWScheduler} annotation.
 * @see com.siiruo.wscheduler.client.annotation.EnableWScheduler
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerAutoDetectorRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        WSchedulerBannerVersion.print();
        WSchedulerAutoDetectorUtil.buildClientIfNecessary();
        WSchedulerAutoDetectorUtil.registerWSchedulerLifecycleProcessorIfNecessary(registry,null);
        WSchedulerAutoDetectorUtil.registerWSchedulerAutoDetectorIfNecessary(registry,null);
    }
}
