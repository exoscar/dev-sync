package org.devsync.spring.watcher.repository;

import org.devsync.spring.watcher.entity.IssueWatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueWatcherRepository extends JpaRepository<IssueWatcher, UUID> {
    Optional<IssueWatcher> findByIssueIdAndUserId(UUID issueId, UUID userId);

    boolean existsByIssueIdAndUserId(UUID issueId, UUID userId);

    List<IssueWatcher> findByIssueId(UUID issueId);
}
