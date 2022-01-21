package com.tools.listener;

import com.tools.event.TestEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 非注解监听器
 */
@Component
public class TestOneListener implements ApplicationListener<TestEvent> {

    @Override
    public void onApplicationEvent(TestEvent event) {
        System.out.println("第一种方式：非注解监听器：" + event.getMessage());
    }

}
