package com.absurd.rick.proxy;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * Created by wangwenwei on 2017/8/31.
 */
public class ProxyJDKTest {
    @Test
    public void jdkProxy1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
    //生成$Proxy0的class文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //获取动态代理类
        Class proxyClazz = Proxy.getProxyClass(HelloWorld.class.getClassLoader(),HelloWorld.class);

        //获得代理类的构造函数，并传入参数类型InvocationHandler.class
        Constructor constructor = proxyClazz.getConstructor(InvocationHandler.class);
        //通过构造函数来创建动态代理对象，将自定义的InvocationHandler实例传入
        HelloWorld proxy = (HelloWorld) constructor.newInstance(new CustomInvocationHandler(new HelloWorldImpl()));
        proxy.sayHello("Mikan");
    }

    @Test
    public void jdkProxy2(){
        //生成$Proxy0的class文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        CustomInvocationHandler handler = new CustomInvocationHandler(new HelloWorldImpl());
        HelloWorld proxy = (HelloWorld) Proxy.newProxyInstance(
                ProxyJDKTest.class.getClassLoader(),
                new Class[]{HelloWorld.class},
                handler);
        proxy.sayHello("Mikan");
    }
}
