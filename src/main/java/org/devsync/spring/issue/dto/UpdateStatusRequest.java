package org.devsync.spring.issue.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.issue.entity.IssueStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {
    @NotBlank
    private IssueStatus status;
}
