package org.devsync.spring.issue.repository;

import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.devsync.spring.issue.projection.IssuePriorityCountProjection;
import org.devsync.spring.issue.projection.IssueStatusCountProjection;
import org.devsync.spring.issue.projection.MemberIssueStatsProjection;
import org.devsync.spring.search.projection.IssueSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID>, JpaSpecificationExecutor<Issue> {

    Page<Issue> findByProjectId(UUID projectId, Pageable pageable);

    Optional<Issue> findByProjectIdAndId(UUID projectId, UUID id);

    long countByProjectId(UUID projectId);

    long countByProjectIdAndStatus(UUID projectId, IssueStatus status);

    long countByProjectIdAndPriority(UUID projectId, IssuePriority priority);

    long countByProjectIdAndAssigneeIsNotNull(UUID projectId);

    long countByProjectIdAndAssigneeIsNull(UUID projectId);

    long countByProjectWorkspaceId(UUID projectWorkspaceId);

    @Query("""
                SELECT
                 i.status as status,
                 COUNT(i) as count
                 FROM Issue i
                 where
                  i.project.workspace.id = :workspaceId
                   group by i.status
            """)
    List<IssueStatusCountProjection> getIssueStatusCountByWorkspaceId(UUID workspaceId);

    @Query("""
                SELECT
                 i.status as status,
                 COUNT(i) as count
                 FROM Issue i
                 where
                  i.project.id = :projectId
                   group by i.status
            """)
    List<IssueStatusCountProjection> getIssueStatusCountByProjectId(UUID projectId);

    @Query("""
            SELECT 
                i.priority as priority,
                COUNT(i) as count 
                from Issue i 
                where 
                    i.project.id = :projectId
                        group by i.priority
            """)
    List<IssuePriorityCountProjection> getIssuePriorityCount(UUID projectId);

    @Query("""
            SELECT
            i.assignee.id AS userId,
            count(i) AS count
            FROM Issue i
            where
                i.project.workspace.id = :workspaceId
                AND
                i.assignee IS NOT NULL
            GROUP BY i.assignee.id
            """)
    List<MemberIssueStatsProjection> getAssignedIssueCounts(UUID workspaceId);

    @Query("""
            SELECT
            i.assignee.id AS userId,
            count(i) AS count
            FROM Issue i
            where
                i.project.workspace.id = :workspaceId
                AND i.assignee IS NOT NULL
                AND i.status = org.devsync.spring.issue.entity.IssueStatus.DONE
            GROUP BY i.assignee.id""")
    List<MemberIssueStatsProjection> getCompletedIssueCounts(UUID workspaceId);

    @Query(value = """
            SELECT
                i.id,
                i.title,
                i.status,
                ts_rank(
                    i.search_vector,
                    websearch_to_tsquery('english', :query)
                ) AS rank,
                    p.id AS projectId,
                    p.name AS projectName
            FROM issue i
            JOIN project p
                ON p.id = i.project_id
            WHERE p.workspace_id = :workspaceId
              AND i.search_vector @@ websearch_to_tsquery('english', :query)
            ORDER BY rank DESC
            LIMIT 15
            """, nativeQuery = true)
    List<IssueSearchProjection> searchIssues(
            @Param("workspaceId") UUID workspaceId,
            @Param("query") String query
    );

}
