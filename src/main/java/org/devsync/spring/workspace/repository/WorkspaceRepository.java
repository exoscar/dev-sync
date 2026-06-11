package org.devsync.spring.workspace.repository;

import org.devsync.spring.workspace.entity.Workspace;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    boolean existsByNameIgnoreCase(String name);

    List<Workspace> findAllByOwnerId(UUID ownerId);




}
