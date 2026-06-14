package org.devsync.spring.workspace.repository;

import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {
    boolean existsByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);

    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);

    List<WorkspaceMember> findByWorkspaceId(UUID workspaceId);

    @Query("""
    select wm.workspace
    from WorkspaceMember wm
    where wm.user.id = :userId
""")
    List<Workspace> findWorkspacesByUserId(UUID userId);


    Optional<WorkspaceMember> findByWorkspaceIdAndId(UUID workspaceId, UUID id);

    long countByWorkspaceId(UUID workspaceId);
}
