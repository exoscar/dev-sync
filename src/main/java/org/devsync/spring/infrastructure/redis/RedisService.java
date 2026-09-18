package org.devsync.spring.infrastructure.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    public void set(
            String key,
            Object value,
            Duration ttl
    ) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            log.warn("Redis SET failed for key={}", key, e);
        }
    }

    public <T> Optional<T> get(
            String key,
            Class<T> type
    ) {
        try {
            Object value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return Optional.empty();
            }

            return Optional.of(
                    objectMapper.convertValue(value, type)
            );
        } catch (Exception e) {
            log.warn("Redis GET failed for key={}", key, e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> get(
            String key,
            TypeReference<T> typeReference
    ) {
        try {
            Object value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return Optional.empty();
            }

            T converted = objectMapper.convertValue(value, typeReference);

            return Optional.of(converted);
        } catch (Exception e) {
            log.warn("Redis GET failed for key={}", key, e);
            return Optional.empty();
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis DELETE failed for key={}", key, e);
        }
    }

    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.warn("Redis EXISTS failed for key={}", key, e);
            return false;
        }
    }

    public Long getTtl(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            log.warn("Redis TTL lookup failed for key={}", key, e);
            return -1L;
        }
    }
}