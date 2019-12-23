package com.siiruo.wscheduler.client.annotation;

import com.siiruo.wscheduler.client.context.WSchedulerAutoDetectorRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by siiruo wong on 2019/12/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({WSchedulerAutoDetectorRegistrar.class})
public @interface EnableWScheduler{
}
