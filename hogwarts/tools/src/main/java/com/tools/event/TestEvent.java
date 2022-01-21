package com.tools.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class TestEvent extends ApplicationEvent {

    private String message;

    public TestEvent(String message) {
        super(message);
        this.message = message;
        log.info("TestEvent message {}", message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
