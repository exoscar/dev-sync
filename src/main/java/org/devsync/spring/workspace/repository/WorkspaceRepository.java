package org.devsync.spring.workspace.repository;

import org.devsync.spring.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    boolean existsByNameIgnoreCase(String name);

    List<Workspace> findAllByOwnerId(UUID ownerId);




}
