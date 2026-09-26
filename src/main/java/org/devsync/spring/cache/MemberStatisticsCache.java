package org.devsync.spring.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.dashboard.dto.MemberStatisticsResponse;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberStatisticsCache {
    private final static Duration TTL = Duration.ofMinutes(2);
    private final RedisService redisService;

    public void put(UUID workspaceId, List<MemberStatisticsResponse> statsResponse) {
        redisService.set(RedisKey.memberStatistics(workspaceId), statsResponse, TTL);
    }

    public Optional<List<MemberStatisticsResponse>> get(UUID workspaceId) {
        return redisService.get(RedisKey.memberStatistics(workspaceId), new TypeReference<List<MemberStatisticsResponse>>() {
        });
    }

    public void evict(UUID workspaceId) {
        redisService.delete(RedisKey.memberStatistics(workspaceId));
    }
}
