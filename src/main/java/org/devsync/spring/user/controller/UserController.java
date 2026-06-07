package org.devsync.spring.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.dto.LoginRequest;
import org.devsync.spring.auth.dto.LoginResponse;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.security.CustomUserDetails;
import org.devsync.spring.common.security.CustomUserDetailsService;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.user.dto.CurrentUserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ApiResponse<CurrentUserResponse> greet(){
        CurrentUserResponse res = currentUserService.getCurrentUserResponse();
        return ApiResponseUtil.success(res,"User Logged In");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "admin accessed";
    }
}
