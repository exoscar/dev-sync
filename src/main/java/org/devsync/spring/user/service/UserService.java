package org.devsync.spring.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.user.dto.UpdateRoleRequest;
import org.devsync.spring.user.dto.UserResponse;
import org.devsync.spring.user.entity.Role;
import org.devsync.spring.user.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public Page<UserResponse> getAllUsers(int page,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id"));
        Page<User> users = userRepository.findAll(pageable);
       return users.map(this::mapToResponse);
    }

    @Transactional
    public UserResponse updateRole(String id, @Valid UpdateRoleRequest request) {
        UUID userId = parseUserId(id);
        User user = getUserById(userId);
        Role role = roleRepository.findByRole(request.getRole()).orElseThrow(
                ()-> new BusinessException("Role Not Found", ErrorCode.NOT_FOUND)
        );
        user.setRole(role);
        return mapToResponse(user);
    }

    public UserResponse getUserById(String id) {
        UUID userId = parseUserId(id);
        return mapToResponse(getUserById(userId));
    }

    private UserResponse mapToResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().getRole())
                .email(user.getEmail())
                .build();
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User Not Found",
                                ErrorCode.NOT_FOUND
                        )
                );
    }

    private UUID parseUserId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(
                    "Invalid User Id",
                    ErrorCode.BAD_REQUEST
            );
        }
    }


}
