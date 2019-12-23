package com.siiruo.wscheduler.client.annotation;

import java.lang.annotation.*;

/**
 * Created by siiruo wong on 2019/12/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface WScheduler {
    String name() default "";
    String before() default "before";
    String after() default "after";
}
