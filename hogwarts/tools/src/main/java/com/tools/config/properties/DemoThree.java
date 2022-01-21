package com.tools.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 获取配置信息 @PropertySource + @Value
 */
@Component
@PropertySource(value = {"classpath:config/demo.properties"})
public class DemoThree {

    @Value("${demo-three.name}")
    private String name;
    @Value("${demo-three.age}")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DemoProperties{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
