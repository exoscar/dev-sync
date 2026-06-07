package org.devsync.spring.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.dto.LoginRequest;
import org.devsync.spring.auth.dto.LoginResponse;
import org.devsync.spring.auth.dto.RegisterRequest;
import org.devsync.spring.auth.dto.RegisterResponse;
import org.devsync.spring.auth.service.AuthService;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse response = authService.registerUser(registerRequest);
        return ApiResponseUtil.success(response,"User registration successful");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.loginUser(loginRequest);
        return ApiResponseUtil.success(response,"User Logged In");
    }

}
