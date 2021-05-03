package com.acme.caas.config;

import com.redislabs.modules.rejson.JReJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public JReJSON redisJsonClient(
        @Value("${spring.redis.host}") String host,
        @Value("${spring.redis.port}") Integer port){
        JReJSON client = new JReJSON(host, port);
        return client;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisTemplate template) {
        return template;
    }
}
