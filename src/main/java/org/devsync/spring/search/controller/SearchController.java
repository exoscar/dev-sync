package org.devsync.spring.search.controller;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.search.dto.GlobalSearchResponse;
import org.devsync.spring.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspaces/{workspaceId}")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ApiResponse<GlobalSearchResponse> globalSearch(@PathVariable UUID workspaceId, @RequestParam String query){
        GlobalSearchResponse response = searchService.search(workspaceId,query);
        return ApiResponseUtil.success(response);
    }
}
