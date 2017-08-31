package com.absurd.rick.proxy;

/**
 * Created by wangwenwei on 2017/8/31.
 */
public class HelloWorldImpl implements HelloWorld{
    @Override
    public void sayHello(String name) {
        System.out.println("Hello " + name);
    }
}
