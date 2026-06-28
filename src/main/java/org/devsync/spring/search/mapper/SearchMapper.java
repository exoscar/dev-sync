package org.devsync.spring.search.mapper;

import org.devsync.spring.search.dto.GlobalSearchResponse;
import org.devsync.spring.search.dto.IssueSearchResult;
import org.devsync.spring.search.dto.ProjectSearchResult;
import org.devsync.spring.search.dto.UserSearchResult;
import org.devsync.spring.search.projection.IssueSearchProjection;
import org.devsync.spring.search.projection.ProjectSearchProjection;
import org.devsync.spring.search.projection.UserSearchProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchMapper {
    public IssueSearchResult toIssueSearchResult(IssueSearchProjection issueSearchProjection){
        return IssueSearchResult.builder()
                .id(issueSearchProjection.getId())
                .title(issueSearchProjection.getTitle())
                .status(issueSearchProjection.getStatus())
                .projectId(issueSearchProjection.getProjectId())
                .projectName(issueSearchProjection.getProjectName())
                .build();
    }

    public ProjectSearchResult toProjectSearchResult(ProjectSearchProjection projectSearchProjection){
        return ProjectSearchResult.builder()
                .id(projectSearchProjection.getId())
                .name(projectSearchProjection.getName())
                .build();
    }
    public UserSearchResult toUserSearchResult(UserSearchProjection userSearchProjection){
        return UserSearchResult.builder()
                .id(userSearchProjection.getId())
                .fullName(userSearchProjection.getFirstName()+" "+ userSearchProjection.getLastName())
                .email(userSearchProjection.getEmail()).build();
    }

    public GlobalSearchResponse toResponse(List<IssueSearchResult> issueSearchResults,
                                           List<ProjectSearchResult> projectSearchResult,
                                           List<UserSearchResult> userSearchResult){
        return GlobalSearchResponse.builder()
                .issues(issueSearchResults)
                .projects(projectSearchResult)
                .users(userSearchResult)
                .build();

    }
}
