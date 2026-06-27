package org.devsync.spring.attachment.mapper;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.attachment.dto.AttachmentResponse;
import org.devsync.spring.attachment.entity.Attachment;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {
    public AttachmentResponse toResponse(Attachment attachment){
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .fileName(attachment.getOriginalFileName())
                .contentType(attachment.getContentType())
                .fileSize(attachment.getFileSize())
                .uploadedBy(attachment.getUploadedBy().getId())
                .uploadedAt(attachment.getCreatedAt())
                .build();
    }


}
