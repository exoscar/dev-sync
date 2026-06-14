package org.devsync.spring.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.issue.projection.IssuePriorityCountProjection;
import org.devsync.spring.issue.projection.IssueStatusCountProjection;
import org.devsync.spring.issue.projection.MemberIssueStatsProjection;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.project.repository.ProjectRepository;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardQueryService {
    private final ProjectRepository projectRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final IssueRepository issueRepository;

    public long countProjects(UUID workspaceId) {
        return projectRepository.countByWorkspaceId(workspaceId);
    }

    public long countMembers(UUID workspaceId) {
        return workspaceMemberRepository.countByWorkspaceId(workspaceId);
    }

    public long countIssuesByWorkspaceId(UUID workspaceId) {
        return issueRepository.countByProjectWorkspaceId(workspaceId);
    }

    public List<IssueStatusCountProjection> getIssueStatusCountsByWorkspaceId(UUID workspaceId) {
        return issueRepository.getIssueStatusCountByWorkspaceId(workspaceId);
    }

    public List<IssueStatusCountProjection> getIssueStatusCountsByProjectId(UUID projectId) {
        return issueRepository.getIssueStatusCountByProjectId(projectId);
    }

    public List<IssuePriorityCountProjection> getIssuePriorityCounts(UUID projectId) {
        return issueRepository.getIssuePriorityCount(projectId);
    }

    public long countIssuesByProjectId(UUID projectId) {
        return issueRepository.countByProjectId(projectId);
    }

    public long countAssignedIssues(UUID projectId) {
        return issueRepository.countByProjectIdAndAssigneeIsNotNull(projectId);
    }

    public long countUnassignedIssues(UUID projectId) {
        return issueRepository.countByProjectIdAndAssigneeIsNull(projectId);
    }

    public Map<UUID,Long> getMemberAssignedIssueStats(UUID workspaceId){
        List<MemberIssueStatsProjection> stats = issueRepository.getAssignedIssueCounts(workspaceId);
        HashMap<UUID,Long> map = new HashMap<>();
        for(MemberIssueStatsProjection stat:stats){
            map.put(stat.getUserId(),stat.getCount());
        }
        return map;
    }

    public Map<UUID,Long> getCompletedIssueCounts(UUID workspaceId){
        List<MemberIssueStatsProjection> statsProjections = issueRepository.getCompletedIssueCounts(workspaceId);
        HashMap<UUID,Long> map = new HashMap<>();
        for(MemberIssueStatsProjection stat: statsProjections){
            map.put(stat.getUserId(),stat.getCount());
        }
        return map;
    }

}
