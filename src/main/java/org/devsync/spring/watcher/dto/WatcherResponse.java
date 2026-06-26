package org.devsync.spring.watcher.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.watcher.entity.WatcherSource;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class WatcherResponse {
    private UUID userId;
    private String name;
    private String email;

    private WatcherSource source;
    private Instant watchedAt;
}
