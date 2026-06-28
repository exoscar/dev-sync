package org.devsync.spring.search.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.project.repository.ProjectRepository;
import org.devsync.spring.search.dto.GlobalSearchResponse;
import org.devsync.spring.search.dto.IssueSearchResult;
import org.devsync.spring.search.dto.ProjectSearchResult;
import org.devsync.spring.search.dto.UserSearchResult;
import org.devsync.spring.search.mapper.SearchMapper;
import org.devsync.spring.search.projection.IssueSearchProjection;
import org.devsync.spring.search.projection.ProjectSearchProjection;
import org.devsync.spring.search.projection.UserSearchProjection;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.devsync.spring.workspace.service.WorkspaceValidationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final SearchMapper mapper;

    public GlobalSearchResponse search(UUID workspaceId, String query){
        validateQuery(query);
        Workspace workspace = workspaceAccessService.getWorkspaceWithMembershipCheck(workspaceId);
        List<IssueSearchProjection> issueSearchProjections = issueRepository.searchIssues(workspace.getId(),query);
        List<ProjectSearchProjection> projectSearchProjections = projectRepository.searchProjects(workspace.getId(),query);
        List<UserSearchProjection> userSearchProjections = userRepository.searchUsers(workspace.getId(),query);
        List<IssueSearchResult> issues = issueSearchProjections.stream().map(mapper::toIssueSearchResult).toList();
        List<ProjectSearchResult> projects = projectSearchProjections.stream().map(mapper::toProjectSearchResult).toList();
        List<UserSearchResult> users = userSearchProjections.stream().map(mapper::toUserSearchResult).toList();
        return mapper.toResponse(issues,projects,users);
    }

    private void validateQuery(String query){
        if(query == null || query.isBlank()){
            throw new BusinessException(
                    "Query cannot be null or blank",
                    ErrorCode.BAD_REQUEST
            );
        }
        if(query.length()>100){
            throw new BusinessException("Query length can not exceet 100 characters",ErrorCode.BAD_REQUEST);
        }
    }
}
