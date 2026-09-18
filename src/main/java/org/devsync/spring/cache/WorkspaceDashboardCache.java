package org.devsync.spring.cache;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.dashboard.dto.WorkspaceDashboardResponse;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceDashboardCache {
    private final RedisService redisService;

    private final static Duration TTL = Duration.ofMinutes(2);

    public void put(
            UUID workspaceId,
            WorkspaceDashboardResponse workspaceDashboardResponse
    ){
        redisService.set(
                RedisKey.workspaceDashboard(
                        workspaceId
                ),
                workspaceDashboardResponse,
                TTL
        );
    }

    public Optional<WorkspaceDashboardResponse> get(
            UUID workspaceId
    ){
      return   redisService.get(
                RedisKey.workspaceDashboard(workspaceId),
                WorkspaceDashboardResponse.class
        );
    }

    public void evict(UUID workspaceId){
        redisService.delete(RedisKey.workspaceDashboard(workspaceId));
    }

}
