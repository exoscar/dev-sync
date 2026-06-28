package org.devsync.spring.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.dto.LoginRequest;
import org.devsync.spring.auth.dto.LoginResponse;
import org.devsync.spring.auth.dto.RegisterRequest;
import org.devsync.spring.auth.dto.RegisterResponse;
import org.devsync.spring.auth.service.AuthService;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.user.dto.CurrentUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register User")
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse response = authService.registerUser(registerRequest);
        return ApiResponseUtil.success(response,"User registration successful");
    }

    @Operation(summary = "User Login")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.loginUser(loginRequest);
        return ApiResponseUtil.success(response,"User Logged In");
    }
}
