package org.devsync.spring.email.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailRecipient {
    private String email;
    private String firstName;
}
