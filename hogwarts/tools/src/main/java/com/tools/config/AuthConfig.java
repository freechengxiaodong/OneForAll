package com.tools.config;

import com.tools.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //需要用时打开
        //registry.addInterceptor(AuthInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthInterceptor AuthInterceptor() {

        return new AuthInterceptor();
    }
}
