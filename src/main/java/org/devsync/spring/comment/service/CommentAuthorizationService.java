package org.devsync.spring.comment.service;

import org.devsync.spring.comment.entity.Comment;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.springframework.stereotype.Component;

@Component
public class CommentAuthorizationService {
    public void requireCommentManagePermission(WorkspaceMember member, Comment comment){
        if( member.getRole() == WorkspaceRole.OWNER
                || member.getRole() == WorkspaceRole.MAINTAINER
                || member.getUser().getId().equals(
                comment.getAuthor().getId()
        )){
            throw new BusinessException("Access Denied: You can not manage comments", ErrorCode.FORBIDDEN);
        }
    }
}
