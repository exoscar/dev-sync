package org.devsync.spring.attachment.service;

import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class AttachmentValidationService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES =
            Set.of(
                    "application/pdf",
                    "image/png",
                    "image/jpeg",
                    "text/plain",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );
    public void validate(MultipartFile file) {
        if(file.isEmpty()){
            throw new BusinessException(
                    "Invalid file",
                    ErrorCode.BAD_REQUEST
            );
        }
        if(file.getSize() > MAX_FILE_SIZE){
            throw new BusinessException(
                    "File size should be less than 10 MB",
                    ErrorCode.BAD_REQUEST
            );
        }
        String contentType = file.getContentType();
        if(contentType == null ||
                !ALLOWED_TYPES.contains(contentType)){
            throw new BusinessException(
                    "Invalid content type",
                    ErrorCode.BAD_REQUEST
            );
        }
    }
}