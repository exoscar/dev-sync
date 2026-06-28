package org.devsync.spring.search.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProjectSearchResult(
        UUID id,
        String name
) {}