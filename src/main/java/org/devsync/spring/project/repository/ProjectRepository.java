package org.devsync.spring.project.repository;

import org.devsync.spring.project.entity.Project;
import org.devsync.spring.search.projection.ProjectSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
SELECT
    p.id AS id,
    p.name AS name,
    ts_rank(
        p.search_vector,
        websearch_to_tsquery('english', :query)
    ) AS rank
FROM project p
WHERE p.workspace_id = :workspaceId
AND p.search_vector @@ websearch_to_tsquery('english', :query)
ORDER BY rank DESC
LIMIT 15
""",nativeQuery = true)
    List<ProjectSearchProjection> searchProjects(   @Param("workspaceId") UUID workspaceId,@Param("query") String query);

}
