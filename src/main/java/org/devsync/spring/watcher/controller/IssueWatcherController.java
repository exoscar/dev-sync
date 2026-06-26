package org.devsync.spring.watcher.controller;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.watcher.dto.WatchStatusResponse;
import org.devsync.spring.watcher.dto.WatcherResponse;
import org.devsync.spring.watcher.service.IssueWatcherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues/{issueId}/watchers")
@RequiredArgsConstructor
public class IssueWatcherController {

    private final IssueWatcherService issueWatcherService;

    @PostMapping("/me")
    public ApiResponse<WatcherResponse> watchIssue(@PathVariable String issueId){
        WatcherResponse response = issueWatcherService.watchIssue(issueId);
        return ApiResponseUtil.success(response);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> unwatchIssue(@PathVariable String issueId){
        issueWatcherService.unwatchIssue(issueId);
        return ApiResponseUtil.success("Issue unwatched");
    }

    @GetMapping()
    public ApiResponse<List<WatcherResponse>> getWatcher(@PathVariable String issueId){
        List<WatcherResponse> watchers = issueWatcherService.getWatchers(issueId);
        return ApiResponseUtil.success(watchers);
    }

    @GetMapping("/me")
    public ApiResponse<WatchStatusResponse> isWatching(@PathVariable String issueId){
        WatchStatusResponse response = issueWatcherService.isWatching(issueId);
        return ApiResponseUtil.success(response);
    }

}
