package org.devsync.spring.watcher.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.service.IssueAccessService;
import org.devsync.spring.issue.service.IssueValidationService;
import org.devsync.spring.watcher.dto.WatchStatusResponse;
import org.devsync.spring.watcher.dto.WatcherResponse;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.devsync.spring.watcher.entity.WatcherSource;
import org.devsync.spring.watcher.mapper.WatcherMapper;
import org.devsync.spring.watcher.repository.IssueWatcherRepository;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.service.WorkspaceAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueWatcherService {

    private final IssueWatcherRepository issueWatcherRepository;
    private final IssueAccessService issueAccessService;
    private final IssueValidationService issueValidationService;
    private final WorkspaceAccessService workspaceAccessService;
    private final WatcherMapper mapper;

    @Transactional
    public WatcherResponse watchIssue(String issueId) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(issue.getProject().getWorkspace().getId());
        if(issueWatcherRepository.existsByIssueIdAndUserId(issueUUID,member.getUser().getId())){
           throw new BusinessException("Already Watching this issue", ErrorCode.WATCHER_ALREADY_EXISTS);
        }
        IssueWatcher watcher = new IssueWatcher();
        watcher.setIssue(issue);
        watcher.setUser(member.getUser());
        watcher.setSource(WatcherSource.MANUAL);
        issueWatcherRepository.save(watcher);
        return mapper.toResponse(watcher);
    }

    @Transactional
    public void addWatcher(Issue issue, User user, WatcherSource source){

        if(issueWatcherRepository.existsByIssueIdAndUserId(issue.getId(),user.getId())){
          return;
        }
        IssueWatcher watcher = new IssueWatcher();
        watcher.setIssue(issue);
        watcher.setUser(user);
        watcher.setSource(source);
        issueWatcherRepository.save(watcher);
    }

    @Transactional
    public void addCreatorWatcher(Issue issue,User currentUser){
        addWatcher(issue, currentUser,WatcherSource.CREATOR);
    }
    @Transactional
    public void addAssigneeWatcher(Issue issue,User assignee){
        addWatcher(issue,assignee,WatcherSource.ASSIGNEE);

    }


    @Transactional
    public void unwatchIssue(String issueId) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(issue.getProject().getWorkspace().getId());
        IssueWatcher watcher = issueWatcherRepository.findByIssueIdAndUserId(issueUUID,member.getUser().getId()).orElseThrow(
                ()-> new BusinessException("Not watching this issue", ErrorCode.NOT_FOUND)
        );
        issueWatcherRepository.delete(watcher);
    }
    @Transactional(readOnly = true)
    public List<WatcherResponse> getWatchers(String issueId){
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        workspaceAccessService.getWorkspaceWithMembershipCheck(issue.getProject().getWorkspace().getId());
        List<IssueWatcher> watchers = issueWatcherRepository.findByIssueId(issueUUID);
        return watchers.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public WatchStatusResponse isWatching(String issueId){
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = issueAccessService.getIssue(issueUUID);
        WorkspaceMember member = workspaceAccessService.getCurrentWorkspaceMember(issue.getProject().getWorkspace().getId());
        boolean watching = issueWatcherRepository.existsByIssueIdAndUserId(issueUUID,member.getUser().getId());
        return WatchStatusResponse.builder().watching(watching).build();
    }

}
