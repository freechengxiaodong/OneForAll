package com.netty.server.config;

import com.netty.server.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(AuthInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthInterceptor AuthInterceptor() {

        return new AuthInterceptor();
    }
}
