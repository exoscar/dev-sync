package org.devsync.spring.comment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.activity.entity.ActivityType;
import org.devsync.spring.activity.service.IssueActivityService;
import org.devsync.spring.comment.dto.CommentResponse;
import org.devsync.spring.comment.dto.CreateCommentRequest;
import org.devsync.spring.comment.dto.UpdateCommentRequest;
import org.devsync.spring.comment.entity.Comment;
import org.devsync.spring.comment.event.CommentCreatedEvent;
import org.devsync.spring.comment.repository.CommentRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.Utils;
import org.devsync.spring.issue.entity.Issue;
import org.devsync.spring.issue.repository.IssueRepository;
import org.devsync.spring.issue.service.IssueValidationService;
import org.devsync.spring.project.entity.Project;
import org.devsync.spring.project.service.ProjectAccessService;
import org.devsync.spring.project.service.ProjectValidationService;
import org.devsync.spring.workspace.entity.Workspace;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.devsync.spring.workspace.repository.WorkspaceMemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CurrentUserService currentUserService;
    private final CommentRepository commentRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final IssueRepository issueRepository;
    private final IssueActivityService issueActivityService;
    private final ProjectAccessService projectAccessService;
    private final CommentAuthorizationService authorizationService;
    private final IssueValidationService issueValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(String issueId, @Valid CreateCommentRequest request) {
        UUID issueUUID = issueValidationService.parseIssueId(issueId);
        Issue issue = getIssue(issueUUID);
        WorkspaceMember member = projectAccessService.getCurrentProjectMember(issue.getProject());
        Comment comment = new Comment();
        comment.setContent(request.getContent().trim());
        comment.setAuthor(member.getUser());
        comment.setIssue(issue);
        comment = commentRepository.save(comment);
        issueActivityService.recordActivity(issue,
                member.getUser(),
                ActivityType.COMMENT_ADDED,
                "Added a Comment");
        eventPublisher.publishEvent(new CommentCreatedEvent(
                issueUUID,
                comment.getId(),
                member.getUser().getId(),
                issue.getProject().getWorkspace().getId(),
                issue.getProject().getId(),
                member.getUser().getFirstName(),
                issue.getTitle()
        ));
        return mapToCommentResponse(comment);
    }


    public Page<CommentResponse> getComments(String issueId,int page,int size) {
        UUID issueUUID =  issueValidationService.parseIssueId(issueId);
        Issue issue = getIssue(issueUUID);
        projectAccessService.getCurrentProjectMember(issue.getProject());
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> comments = commentRepository.findByIssueId(issueUUID,pageable);
        return comments.map(this::mapToCommentResponse);
    }
    @Transactional
    public CommentResponse updateComment(String commentId, @Valid UpdateCommentRequest updateCommentRequest) {
        UUID commentUUID = parseCommentId(commentId);
        Comment comment = commentRepository.findById(commentUUID).orElseThrow(()->
                new BusinessException("Comment not found",ErrorCode.NOT_FOUND));
        Project project = comment.getIssue().getProject();
        WorkspaceMember member = projectAccessService.getCurrentProjectMember(project);
        authorizationService.requireCommentManagePermission(member,comment);
        String content =
                updateCommentRequest.getContent().trim();

        if(content.equals(comment.getContent())){
            throw new BusinessException(
                    "Comment content is unchanged",
                    ErrorCode.BAD_REQUEST
            );
        }
        comment.setContent(content);
        issueActivityService.recordActivity(comment.getIssue(),
                member.getUser(),
                ActivityType.COMMENT_UPDATED,
                "Updated a Comment");
        return mapToCommentResponse(comment);
    }

    @Transactional
    public void deleteComment(String commentId){
        UUID commentUUID = parseCommentId(commentId);
        Comment comment = commentRepository.findById(commentUUID).orElseThrow(()->
                new BusinessException("Comment not found",ErrorCode.NOT_FOUND));
        Project project = comment.getIssue().getProject();
        WorkspaceMember member = projectAccessService.getCurrentProjectMember(project);
       authorizationService.requireCommentManagePermission(member,comment);
        issueActivityService.recordActivity(comment.getIssue(),
                member.getUser(),
                ActivityType.COMMENT_DELETED,
                "Deleted a Comment");
        commentRepository.delete(comment);
    }


    private CommentResponse mapToCommentResponse(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorEmail(comment.getAuthor().getEmail())
                .issueId(comment.getIssue().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }



    private UUID parseCommentId(String id) {
        return Utils.parseUuid(id, "Invalid Comment Id");
    }

    private Issue getIssue(UUID issueUUID) {
        return issueRepository.findById(issueUUID).orElseThrow(
                () -> new BusinessException("Issue not found", ErrorCode.NOT_FOUND)
        );
    }




}
