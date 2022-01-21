package com.spring.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringJpaApplication {

    public static void main(String[] args) {

        ApplicationContext ac = SpringApplication.run(SpringJpaApplication.class, args);
        System.out.println(ac);
    }

}
