package org.devsync.spring.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.constants.AppConstants;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.user.dto.CurrentUserResponse;
import org.devsync.spring.user.dto.UpdateRoleRequest;
import org.devsync.spring.user.dto.UserResponse;
import org.devsync.spring.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE+"") int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE+"") int size
    ){
        Page<UserResponse> users = userService.getAllUsers(page,size);
        return ApiResponseUtil.success(users);
    }

    @PutMapping("/{id}/role")
    public ApiResponse<UserResponse> updateRole(@Valid @RequestBody UpdateRoleRequest request,
                                                @PathVariable String id){
    UserResponse userResponse = userService.updateRole(id,request);
    return ApiResponseUtil.success(userResponse,"Role Updated Successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id){
        UserResponse userResponse = userService.getUserById(id);
        return ApiResponseUtil.success(userResponse);
    }
}
