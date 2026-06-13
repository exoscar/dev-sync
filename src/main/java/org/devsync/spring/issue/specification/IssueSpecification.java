package org.devsync.spring.issue.specification;

import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class IssueSpecification {
    public static Specification<Issue> hasProject(UUID projectId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                root.get("project").get("id"),
                projectId
        );
    }

    public static Specification<Issue> hasPriority(IssuePriority priority){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("priority"),
                priority
        );
    }

    public static Specification<Issue> hasStatus(
            IssueStatus status
    ){
        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Issue> hasAssignee(
            UUID assigneeId
    ){
        return (root, query, cb) ->
                cb.equal(
                        root.get("assignee").get("id"),
                        assigneeId
                );
    }

}
