package com.absurd.rick.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware{
    private static ApplicationContext applicationContext; // spring应用上下文环境
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public static Collection<Object> getBeans(String ...names){
        Collection<Object> beans = new ArrayList<Object>();
        for(String name:names){
            beans.add(applicationContext.getBean(name));
        }
        return beans;
    }

    public static Collection<Object> getBeanByType(Class<?> type){
        return getBeans(applicationContext.getBeanNamesForType(type));
    }
}
