package com.absurd.rick;

import com.absurd.rick.event.support.Event;
import com.absurd.rick.event.support.spring.SpringAppEvent;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created by wangwenwei on 17/8/2.
 */
public class EventTest extends SpringMvcTest{

    @Autowired
    private ApplicationContext context;


    @Test
    public void springTest(){
        Event event = new Event();
        event.setOperator("auth.login");
        event.setData(Lists.newArrayList("a","b"));
        //方法中调用
        context.publishEvent(new SpringAppEvent(this, event));
    }
}
