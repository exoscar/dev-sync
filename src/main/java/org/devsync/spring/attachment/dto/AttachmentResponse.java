package org.devsync.spring.attachment.dto;

import lombok.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder
public record AttachmentResponse(
        UUID id,
        String fileName,
        String contentType,
        Long fileSize,
        UUID uploadedBy,
        Instant uploadedAt
) {
}
