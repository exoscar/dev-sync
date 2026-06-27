package org.devsync.spring.attachment.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.attachment.dto.AttachmentDownloadResponse;
import org.devsync.spring.attachment.dto.AttachmentResponse;
import org.devsync.spring.attachment.entity.Attachment;
import org.devsync.spring.attachment.mapper.AttachmentMapper;
import org.devsync.spring.attachment.repository.AttachmentRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.context.IssueContext;
import org.devsync.spring.issue.service.IssueAccessService;
import org.devsync.spring.issue.service.IssueAuthorizationService;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository repository;
    private final FileStorageService fileStorageService;
    private final AttachmentValidationService validationService;
    private final IssueAccessService issueAccessService;
    private final IssueAuthorizationService issueAuthorizationService;
    private final AttachmentMapper mapper;

    @Transactional
    public AttachmentResponse uploadFile(String projectId, String issueId, MultipartFile file) {
        IssueContext issueContext = issueAccessService.loadIssueContext(projectId, issueId);
        issueAuthorizationService.requireContributor(issueContext.member());
        validationService.validate(file);
        String storedFileName = fileStorageService.store(file);
        try {
            Attachment attachment = new Attachment();
            attachment.setFileSize(file.getSize());
            attachment.setUploadedBy(issueContext.member().getUser());
            attachment.setContentType(file.getContentType());
            attachment.setOriginalFileName(file.getOriginalFilename());
            attachment.setStoredFileName(storedFileName);
            attachment.setIssue(issueContext.issue());
            repository.save(attachment);
            return mapper.toResponse(attachment);
        } catch (RuntimeException e) {
            fileStorageService.delete(storedFileName);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<AttachmentResponse> getAttachments(String projectId, String issueId) {
        IssueContext issueContext = issueAccessService.loadIssueContext(projectId, issueId);
        List<Attachment> attachments = repository.findByIssueId(issueContext.issue().getId());
        return attachments.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AttachmentDownloadResponse getAttachmentResource(String projectId, String issueId, String attachmentId) {
        IssueContext issueContext = issueAccessService.loadIssueContext(projectId, issueId);

        Attachment attachment = getAttachment(attachmentId, issueContext);
        Resource resource = fileStorageService.load(attachment.getStoredFileName());
        return AttachmentDownloadResponse.builder()
                .resource(resource)
                .originalFilename(attachment.getOriginalFileName())
                .contentType(attachment.getContentType()).build();
    }


    @Transactional
    public void deleteAttachment(String projectId, String issueId, String attachmentId) {
        IssueContext issueContext = issueAccessService.loadIssueContext(projectId, issueId);
        issueAuthorizationService.requireContributor(issueContext.member());
        Attachment attachment = getAttachment(attachmentId, issueContext);
        fileStorageService.delete(attachment.getStoredFileName());
        repository.delete(attachment);
    }

    private UUID parseAttachmentId(String attachmentId){
        return Utils.parseUuid(attachmentId,"Invalid Attachment Id");
    }

    private Attachment getAttachment(String attachmentId, IssueContext issueContext) {
        Attachment attachment = repository.findById(parseAttachmentId(attachmentId)).orElseThrow(
                ()-> new BusinessException("File not found",ErrorCode.NOT_FOUND)
        );
        if(!attachment.getIssue().getId()
                .equals(issueContext.issue().getId())){
            throw new BusinessException(
                    "Attachment not found",
                    ErrorCode.NOT_FOUND
            );
        }
        return attachment;
    }
}
