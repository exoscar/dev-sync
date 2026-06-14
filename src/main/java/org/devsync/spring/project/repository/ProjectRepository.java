package org.devsync.spring.project.repository;

import org.devsync.spring.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByWorkspaceId(UUID workspaceId);

    Optional<Project> findByWorkspaceIdAndId(UUID workspaceId, UUID id);

    boolean existsByWorkspaceIdAndNameIgnoreCase(UUID workspaceId, String name);

    long countByWorkspaceId(UUID workspaceId);

}
