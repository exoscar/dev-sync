package org.devsync.spring.cache;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.dashboard.dto.ProjectStatsResponse;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectDashboardCache {
    private final RedisService redisService;
    private final static Duration TTL = Duration.ofMinutes(2);

    public void put(UUID projectId, ProjectStatsResponse statsResponse) {
        redisService.set(RedisKey.projectDashboard(projectId), statsResponse, TTL);
    }
    public Optional<ProjectStatsResponse> get(UUID projectId){
      return redisService.get(RedisKey.projectDashboard(projectId), ProjectStatsResponse.class);
    }
    public void evict(UUID projectId){
        redisService.delete(RedisKey.projectDashboard(projectId));
    }
}
