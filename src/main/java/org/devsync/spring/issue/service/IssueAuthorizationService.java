package org.devsync.spring.issue.service;

import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.workspace.entity.WorkspaceMember;
import org.devsync.spring.workspace.entity.WorkspaceRole;
import org.springframework.stereotype.Service;

@Service
public class IssueAuthorizationService {
    public void requireContributor(
            WorkspaceMember member
    ) {
        if(member.getRole() == WorkspaceRole.VIEWER){
            throw new BusinessException(
                    "Access Denied: You do not have enough privileges to perform this task",
                    ErrorCode.FORBIDDEN
            );
        }
    }

    public void requireManager(
            WorkspaceMember member
    ){
        if(!(member.getRole() == WorkspaceRole.OWNER || member.getRole() == WorkspaceRole.MAINTAINER)){
            throw new BusinessException(
                    "Access Denied: You can not delete issue",
                    ErrorCode.FORBIDDEN
            );

        }
    }



}


