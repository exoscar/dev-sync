package org.devsync.spring.user.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.user.entity.Role;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
