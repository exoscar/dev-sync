package org.devsync.spring.watcher.mapper;

import org.devsync.spring.watcher.dto.WatcherResponse;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.springframework.stereotype.Component;

@Component
public class WatcherMapper {
    public WatcherResponse toResponse(IssueWatcher issueWatcher) {
        return WatcherResponse.builder()
                .userId(issueWatcher.getUser().getId())
                .name(issueWatcher.getUser().getFirstName())
                .email(issueWatcher.getUser().getEmail())
                .source(issueWatcher.getSource())
                .watchedAt(issueWatcher.getCreatedAt())
                .build();
    }
}
