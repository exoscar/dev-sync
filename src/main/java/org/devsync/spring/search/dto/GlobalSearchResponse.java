package org.devsync.spring.search.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GlobalSearchResponse(
        List<IssueSearchResult> issues,
        List<ProjectSearchResult> projects,
        List<UserSearchResult> users
) {}
