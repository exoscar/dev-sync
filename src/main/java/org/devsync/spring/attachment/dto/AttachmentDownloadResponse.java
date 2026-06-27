package org.devsync.spring.attachment.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Builder
@Data
public class AttachmentDownloadResponse {
    private Resource resource;
    private String originalFilename;
    private String contentType;
}
