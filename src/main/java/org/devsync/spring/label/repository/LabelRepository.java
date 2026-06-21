package org.devsync.spring.label.repository;

import org.devsync.spring.label.entity.Label;
import org.devsync.spring.label.entity.LabelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label,UUID> {

    boolean existsByWorkspaceIdAndNormalizedName(UUID workspaceId, String normalizedName);

    List<Label> findAllByWorkspaceIdAndStatus(UUID workspaceId, LabelStatus status);

    Optional<Label> findByWorkspaceIdAndId(UUID workspaceId, UUID id);

    List<Label> findByWorkspaceIdAndStatus(
            UUID workspaceId,
            LabelStatus status
    );

    boolean existsByWorkspaceIdAndNormalizedNameAndIdNot(UUID workspaceId, String normalizedName, UUID id);
}
