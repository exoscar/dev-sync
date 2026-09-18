package org.devsync.spring.cache;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.devsync.spring.workspace.dto.WorkspaceResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceCache {
    private static final Duration TTL = Duration.ofMinutes(30);
    private final RedisService redisService;

    public void put(UUID workspaceId, WorkspaceResponse workspace) {
        redisService.set(
                RedisKey.workspace(workspaceId),
                workspace,
                TTL
        );
    }

    public Optional<WorkspaceResponse> get(UUID workspaceId) {

        return redisService.get(
                RedisKey.workspace(workspaceId),
                WorkspaceResponse.class
        );
    }

    public void evict(UUID workspaceId) {
        redisService.delete(
                RedisKey.workspace(workspaceId)
        );
    }

}
