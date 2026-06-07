package org.devsync.spring.common.security;

import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.user.dto.CurrentUserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    public CustomUserDetails getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal()
                instanceof CustomUserDetails user)) {

            throw new BusinessException(
                    "No authenticated user found",
                    ErrorCode.UNAUTHORIZED
            );
        }
        return user;
    }

    public UUID getCurrentUserId(){
        return getCurrentUser().getUserId();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public CurrentUserResponse getCurrentUserResponse(){
        CustomUserDetails cus = getCurrentUser();
       return CurrentUserResponse.builder().email(cus.getEmail()).userId(cus.getUserId()).build();
    }
}
