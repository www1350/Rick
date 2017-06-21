package com.absurd.rick.util;

import com.absurd.rick.annotation.BeanAlias;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * @author Absurd.
 */
@Slf4j
public class BeanUtilEx {

    public static Map<String,Object> transBean2Map(Object object){
        if (object == null) return null;
        Map<String,Object> map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
                String key = propertyDescriptor.getName();
                if (!"class".equals(key)){
                    Method method = propertyDescriptor.getReadMethod();
                    Object result =  method.invoke(object);
                    map.put(key,result);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object source, Object target,String... ignoreProperties){
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        ignoreProperties = concat(getNullPropertyNames(source), ignoreProperties);
        HashMap<String,String> aliasMap =cacheBeanAlias(source);
        try {
            Class<?> actualEditable = target.getClass();
            PropertyDescriptor[] targetPds = getPropertyDescs(actualEditable);
            List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

            for (PropertyDescriptor targetPd : targetPds) {
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                   String targetProName =  StringUtils.isEmpty(aliasMap.get(targetPd.getName())) ? targetPd.getName() :  aliasMap.get(targetPd.getName());
                    PropertyDescriptor sourcePd = getPropertyDesc(source.getClass(), targetProName);
                    if (sourcePd != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (readMethod != null &&
                                ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            } catch (Throwable ex) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            }
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            log.error("error copy", ex);
            throw new FatalBeanException("error copy", ex);
        }

    }

    private static HashMap<String,String> cacheBeanAlias(Object source) {
        HashMap<String,String> map = Maps.newHashMap();
        Field fs[] = source.getClass().getDeclaredFields();
        for(Field f: fs) {
            BeanAlias beanAlias = f.getAnnotation(BeanAlias.class);
            if (beanAlias == null || StringUtils.isEmpty(beanAlias.value())) {
                map.put(f.getName(),f.getName());
                continue;
            }
            map.put(beanAlias.value(),f.getName());
        }
        return map;

    }

    private static PropertyDescriptor[] getPropertyDescs(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo =  Introspector.getBeanInfo(clazz);
        return beanInfo.getPropertyDescriptors();
    }

    private static PropertyDescriptor getPropertyDesc(Class<?> clazz, String propertyName) {
        PropertyDescriptor propertyDescriptor = null;
        try {
            propertyDescriptor = new PropertyDescriptor(propertyName,clazz);
        } catch (IntrospectionException e) {
            return null;
        }
        return propertyDescriptor;
    }

    private static <T> T[] concat(T[] first, T[] second) {
        if (null == first || first.length == 0) return second;
        if (null == second || second.length == 0) return first;
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    /**
     * 获取 目标对象
     *
     * @param proxy
     *            代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) {

        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;// 不是代理对象
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { // cglib
            return getCglibProxyTargetObject(proxy);
        }

    }

    private static Object getCglibProxyTargetObject(Object proxy) {
        try{
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
            return target;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) {
        try{
            Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            AopProxy aopProxy = (AopProxy) h.get(proxy);
            Field advised = aopProxy.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
            return target;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
