package org.devsync.spring.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class CurrentUserResponse {
    private String email;
    private UUID userId;
}
