package org.devsync.spring.watcher.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WatchStatusResponse {
    private boolean watching;
}