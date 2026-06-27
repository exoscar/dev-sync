package org.devsync.spring.watcher.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.service.IssueAccessService;
import org.devsync.spring.issue.service.IssueValidationService;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.devsync.spring.watcher.repository.IssueWatcherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IssueWatcherAccessService {

    private final IssueValidationService issueValidationService;
    private final IssueAccessService issueAccessService;
    private final IssueWatcherRepository issueWatcherRepository;

    public List<IssueWatcher> getIssueWatchers(UUID issueId){
        Issue issue = issueAccessService.getIssue(issueId);
        List<IssueWatcher> watchers = issueWatcherRepository.findByIssueId(issueId);
        return watchers;
    }
}
