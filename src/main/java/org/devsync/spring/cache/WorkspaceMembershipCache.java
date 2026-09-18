package org.devsync.spring.cache;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.infrastructure.redis.RedisKey;
import org.devsync.spring.infrastructure.redis.RedisService;
import org.devsync.spring.workspace.dto.WorkspaceMembershipCacheEntry;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceMembershipCache {

    private static final Duration TTL =
            Duration.ofMinutes(15);

    private final RedisService redisService;

    public void put(
            UUID workspaceId,
            UUID userId,
            WorkspaceRole role
    ) {
        WorkspaceMembershipCacheEntry membership =
                new WorkspaceMembershipCacheEntry(
                        userId,
                        workspaceId,
                        role
                );

        redisService.set(
                RedisKey.workspaceMembership(
                        workspaceId,
                        userId
                ),
                membership,
                TTL
        );
    }

    public Optional<WorkspaceMembershipCacheEntry> get(
            UUID workspaceId,
            UUID userId
    ) {
        return redisService.get(
                RedisKey.workspaceMembership(
                        workspaceId,
                        userId
                ),
                WorkspaceMembershipCacheEntry.class
        );
    }

    public void evict(
            UUID workspaceId,
            UUID userId
    ) {
        redisService.delete(
                RedisKey.workspaceMembership(
                        workspaceId,
                        userId
                )
        );
    }
}
