package org.devsync.spring.watcher.repository;

import org.devsync.spring.email.dto.EmailRecipient;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueWatcherRepository extends JpaRepository<IssueWatcher, UUID> {
    Optional<IssueWatcher> findByIssueIdAndUserId(UUID issueId, UUID userId);

    boolean existsByIssueIdAndUserId(UUID issueId, UUID userId);

    List<IssueWatcher> findByIssueId(UUID issueId);

    @Query("""
                select new org.devsync.spring.email.dto.EmailRecipient(
                    u.email,
                    u.firstName
                )
                from IssueWatcher iw
                join iw.user u
                where iw.issue.id = :issueId
                  and u.id <> :actorId
            """)
    List<EmailRecipient> findWatcherEmailRecipients(
            UUID issueId,
            UUID actorId
    );
}
