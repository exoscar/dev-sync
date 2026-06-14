package org.devsync.spring.activity.repository;

import org.devsync.spring.activity.entity.IssueActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IssueActivityRepository extends JpaRepository<IssueActivity, UUID> {
    Page<IssueActivity> findByIssueId(
            UUID issueId,
            Pageable pageable
    );

    Page<IssueActivity> findAllByIssueProjectWorkspaceId(UUID issueProjectWorkspaceId, Pageable pageable);
}
