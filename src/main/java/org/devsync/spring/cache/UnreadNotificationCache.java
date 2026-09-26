package org.devsync.spring.cache;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnreadNotificationCache {
    private final static Duration TTL = Duration.ofMinutes(1);
    private final RedisService redisService;

    public void put(UUID userId, Long unreadCnt) {
        redisService.set(RedisKey.unreadNotification(userId), unreadCnt, TTL);
    }

    public Optional<Long> get(UUID userId) {
        return redisService.get(RedisKey.unreadNotification(userId), Long.class);
    }

    public void evict(UUID userId) {
        redisService.delete(RedisKey.unreadNotification(userId));
    }
}
