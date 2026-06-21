package org.devsync.spring.label.repository;

import org.devsync.spring.label.entity.IssueLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IssueLabelRepository extends JpaRepository<IssueLabel, UUID> {
    boolean existsByIssueIdAndLabelId(
            UUID issueId,
            UUID labelId
    );
    

    void deleteByIssueIdAndLabelId(
            UUID issueId,
            UUID labelId
    );

    List<IssueLabel> findByIssueId(UUID issueId);
}
