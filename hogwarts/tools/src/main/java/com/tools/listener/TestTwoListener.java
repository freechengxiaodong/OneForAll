package com.tools.listener;

import com.tools.event.TestEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 注解监听
 */
@Component
public class TestTwoListener {

    @EventListener
    public void listener1(TestEvent event) {
        System.out.println("第二种监听器：注解监听器: " + event.getMessage());
    }
}
