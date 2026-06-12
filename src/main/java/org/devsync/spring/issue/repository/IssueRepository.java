package org.devsync.spring.issue.repository;

import org.devsync.spring.issue.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID> {

    Page<Issue> findByProjectId(UUID projectId, Pageable pageable);

    Optional<Issue> findByProjectIdAndId(UUID projectId, UUID id);
}
