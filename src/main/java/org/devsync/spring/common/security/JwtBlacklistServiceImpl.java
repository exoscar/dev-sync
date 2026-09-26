package org.devsync.spring.common.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    private final RedisService redisService;
    private final JwtService jwtService;

    @Override
    public void blacklist(String token) {

        Claims claims = jwtService.extractClaims(token);
        Duration ttl = Duration.between(
                Instant.now(),
                claims.getExpiration().toInstant()
        );
        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }
        redisService.set(
                RedisKey.jwtBlacklist(claims.getId()),
                Boolean.TRUE,
                ttl
        );
    }

    @Override
    public boolean isBlacklisted(String jti) {
        return redisService.exists(RedisKey.jwtBlacklist(jti));
    }
}
