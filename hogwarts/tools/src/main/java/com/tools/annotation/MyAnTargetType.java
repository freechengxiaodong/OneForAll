package com.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个可以注解在Class,interface,enum上的注解
 *
 * @date 2022年4月22日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnTargetType {

    /**
     * 定义注解的一个元素 并给定默认值
     * @return
     */
    String value() default "我是定义在（类、接口、枚举）类上的注解元素value的默认值";
}
