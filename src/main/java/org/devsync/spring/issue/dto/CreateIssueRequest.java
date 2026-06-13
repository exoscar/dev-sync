package org.devsync.spring.issue.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.issue.entity.IssuePriority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIssueRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @JsonSetter(nulls = Nulls.SKIP)
    private IssuePriority priority= IssuePriority.MEDIUM;
}
