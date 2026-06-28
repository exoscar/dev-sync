package org.devsync.spring.issue.specification;

import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class IssueSpecification {
    public static Specification<Issue> hasProject(UUID projectId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("project").get("id"),
                        projectId
                );
    }

    public static Specification<Issue> hasPriority(IssuePriority priority) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("priority"),
                priority
        );
    }

    public static Specification<Issue> hasStatus(
            IssueStatus status
    ) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Issue> hasAssignee(
            UUID assigneeId
    ) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("assignee").get("id"),
                        assigneeId
                );
    }

    public static Specification<Issue> isUnassigned() {
        return (root, query, cb) ->
                cb.isNull(root.get("assignee"));
    }

    public static Specification<Issue> createdAfter(Instant createdFrom) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        createdFrom
                );
    }

    public static Specification<Issue> createdBefore(Instant createdTo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("createdAt"),
                        createdTo
                );
    }

    public static Specification<Issue> updatedAfter(Instant updatedFrom) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("updatedAt"),
                        updatedFrom
                );
    }

    public static Specification<Issue> updatedBefore(Instant updatedTo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("updatedAt"),
                        updatedTo
                );
    }

    public static Specification<Issue> hasWorkspace(UUID workspaceId) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("project")
                                .get("workspace")
                                .get("id"),
                        workspaceId
                );
    }
}
