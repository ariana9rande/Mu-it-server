package com.hjh.muit.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("local")
public class RedisBasicTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redis_set_get_delete() {
        String key = "test:key";
        String value = "hello_redis";

        // 1. 저장
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(10));

        // 2. 조회
        String result = redisTemplate.opsForValue().get(key);
        System.out.println("🔍 Redis 값: " + result);
        Assertions.assertEquals(value, result);

        // 3. 삭제
        redisTemplate.delete(key);
        String deleted = redisTemplate.opsForValue().get(key);
        Assertions.assertNull(deleted);
    }
}

