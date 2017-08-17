package com.absurd.rick.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware{
    private static final LoadingCache<Class<?>, ImmutableList<Object>> handlerMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, ImmutableList<Object>>() {
                        @Override
                        public ImmutableList<Object> load(Class<?> concreteClass) throws Exception {
                            return getAnnotatedMethodsInternal(concreteClass);
                        }
                    });

    private static ImmutableList<Object> getAnnotatedMethodsInternal(Class<?> type) {
       return ImmutableList.copyOf( getBeanByType(type));
    }

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


    public static ImmutableList<Object> getBeanByTypeWithCache(Class<?> type){
        return handlerMethodsCache.getUnchecked(type);
    }
}
