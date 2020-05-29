package com.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisConfiguration {
 
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() throws Exception {
    	
    	RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    	JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
    	builder.usePooling();
        JedisConnectionFactory factory = new JedisConnectionFactory(config, builder.build());
 
        return factory;
    }
}
