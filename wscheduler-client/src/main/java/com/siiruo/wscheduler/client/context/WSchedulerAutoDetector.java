package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.annotation.WScheduler;
import com.siiruo.wscheduler.client.bean.SingleExecutor;
import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.core.bean.ExecutorParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by siiruo wong on 2019/12/21.
 */
public class WSchedulerAutoDetector implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WSchedulerAutoDetector.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        InternalExecutorParamBuilder builder = resolveFromClass(bean,beanName);
        if (builder==null) {
            builder=resolveFromMethod(bean,beanName);
        }
        if (builder!=null) {
            SingleExecutor executor=new SingleExecutor(builder.target,builder.name,builder.execute,
                                        builder.before,builder.after,builder.init,builder.destroy);
            WSchedulerContextHolder.addExecutor(executor);
        }
        return bean;
    }


    private InternalExecutorParamBuilder resolveFromClass(Object bean, String beanName) throws BeansException{
        WScheduler classAnnotation=AnnotationUtils.findAnnotation(bean.getClass(), WScheduler.class);
        if (classAnnotation==null) {
            return null;
        }
        return buildParamBuilder(bean,classAnnotation);
    }

    private InternalExecutorParamBuilder resolveFromMethod(Object bean, String beanName) throws BeansException{
        if (bean==null){
            return null;
        }
        Class clazz=bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        WScheduler methodAnnotation=null;
        Method executeMethod=null;
        if (methods!=null) {
            for (Method method : methods) {
                methodAnnotation=AnnotationUtils.findAnnotation(method, WScheduler.class);
                if (methodAnnotation!=null) {
                    executeMethod=method;
                    break;
                }
            }
        }
        if (methodAnnotation==null) {
            return null;
        }
        try {
            InvocationHandler handler = Proxy.getInvocationHandler(methodAnnotation);
            Field field = handler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map memberValues = (Map) field.get(handler);
            memberValues.put("execute", executeMethod.getName());
        } catch (NoSuchFieldException e) {
            LOGGER.error("an error occurs when modifying @WScheduler#execute property as {}",executeMethod.getName());
            throw new BeanInitializationException(String.format("modifying @WScheduler#execute property as %s,unable to load w-scheduler",executeMethod.getName()),e);
        } catch (IllegalAccessException e) {
            LOGGER.error("an error occurs when modifying @WScheduler#execute property as {}",executeMethod.getName());
            throw new BeanInitializationException(String.format("modifying @WScheduler#execute property as %s,unable to load w-scheduler",executeMethod.getName()),e);
        }
        return buildParamBuilder(bean,methodAnnotation);
    }


    private InternalExecutorParamBuilder buildParamBuilder(Object bean,WScheduler annotation) throws BeansException{
        Class<?> clazz = bean.getClass();
        String execute = StringUtils.isEmpty(annotation.execute())?WSchedulerConstantType.DEFAULT_EXECUTOR_EXECUTE_METHOD_NAME:annotation.execute();
        String before = StringUtils.isEmpty(annotation.before())?WSchedulerConstantType.DEFAULT_EXECUTOR_BEFORE_METHOD_NAME:annotation.before();
        String after = StringUtils.isEmpty(annotation.after())?WSchedulerConstantType.DEFAULT_EXECUTOR_AFTER_METHOD_NAME :annotation.after();
        String init = StringUtils.isEmpty(annotation.init())?WSchedulerConstantType.DEFAULT_EXECUTOR_INIT_METHOD_NAME:annotation.init();
        String destroy = StringUtils.isEmpty(annotation.destroy())?WSchedulerConstantType.DEFAULT_EXECUTOR_DESTROY_METHOD_NAME :annotation.destroy();

        InternalExecutorParamBuilder builder=new InternalExecutorParamBuilder();
        try {
            builder.execute = clazz.getMethod(execute, ExecutorParameter.class);
            builder.execute.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{} method not found.",execute);
            throw new BeanInitializationException(String.format("%s method not found,unable to load w-scheduler",execute),e);
        }

        try {
            builder.before = clazz.getMethod(before,null);
            builder.before.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{} method not found,related processing will be ignored.",before);
        }
        try {
            builder.after = clazz.getMethod(after,null);
            builder.after.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{} method not found,related processing will be ignored.",after);
        }
        try {
            builder.init = clazz.getMethod(init,null);
            builder.init.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{} method not found,related processing will be ignored.",init);
        }
        try {
            builder.destroy = clazz.getMethod(destroy,null);
            builder.destroy.setAccessible(true);
        } catch (NoSuchMethodException e) {
            LOGGER.error("{} method not found,related processing will be ignored.",destroy);
        }

        builder.target=bean;
        builder.name=StringUtils.isEmpty(annotation.name())?clazz.getName():annotation.name();
        return builder;
    }


    private class InternalExecutorParamBuilder {
        private Object target;
        private String name;
        private Method execute;
        private Method before;
        private Method after;
        private Method init;
        private Method destroy;

        private InternalExecutorParamBuilder() {
        }
    }
}
