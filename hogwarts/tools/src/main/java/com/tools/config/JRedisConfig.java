package com.tools.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
public class JRedisConfig {

    @Bean
    public JedisPoolConfig jedisPoolConfig(JedisProperty jedisProperty) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisProperty.getMaxTotal());
        jedisPoolConfig.setMaxIdle(jedisProperty.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisProperty.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisProperty.getMaxWaitMillis());
        jedisPoolConfig.setTestOnBorrow(jedisProperty.isTestOnBorrow());

        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisProperty jedisProperty, JedisPoolConfig jedisPoolConfig) {

        log.info("=====创建JedisPool连接池=====");
        if (StringUtils.isNotEmpty(jedisProperty.getPassword())) {
            return new JedisPool(jedisPoolConfig, jedisProperty.getHost(), jedisProperty.getPort(), jedisProperty.getTimeout(), jedisProperty.getPassword());
        }

        return new JedisPool(jedisPoolConfig, jedisProperty.getHost(), jedisProperty.getPort(), jedisProperty.getTimeout());
    }
}
