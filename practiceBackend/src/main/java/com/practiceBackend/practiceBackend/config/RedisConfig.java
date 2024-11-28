package com.practiceBackend.practiceBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);
        return new LettuceConnectionFactory(redisConfig);
    }

    //pring Data Redis에서 제공하는 Redis와 애플리케이션 간 데이터를 읽고 쓰기 위한 핵심 클래스입니다.
    // 이를 통해 Redis 서버에 데이터를 저장하거나 조회, 수정, 삭제와 같은 작업을 쉽게 처리할 수 있습니다.
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        //직렬화
        // RedisTemplate 는 JdkSerializationRedisSerializer 를 사용함
        // 데이터를 바이너리로 저장하며, 직렬화된 데이터는 사람이 읽을 수 없고 다른 시스템과 호환성이 낮습니다.

        // redis의 key 를 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redis의 value 를 jason 형태로 직렬화
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        //Redis의 Hash 구조에서 Key(필드)를 문자열로 직렬화합니다.
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //설명: Redis의 Hash 구조에서 Value를 JSON 형식으로 직렬화합니다.
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

}
