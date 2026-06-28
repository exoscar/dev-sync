package org.devsync.spring.search.controller;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.search.dto.GlobalSearchResponse;
import org.devsync.spring.search.mapper.SearchMapper;
import org.devsync.spring.search.service.SearchService;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspaces/{workspaceId}/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ApiResponse<GlobalSearchResponse> globalSearch(@PathVariable UUID workspaceId, @RequestParam String query){
        GlobalSearchResponse response = searchService.search(workspaceId,query);
        return ApiResponseUtil.success(response);
    }
}
