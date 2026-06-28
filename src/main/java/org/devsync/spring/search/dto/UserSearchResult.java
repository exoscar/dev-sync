package org.devsync.spring.search.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserSearchResult(
        UUID id,
        String fullName,
        String email
) {}