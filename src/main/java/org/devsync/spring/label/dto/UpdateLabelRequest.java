package org.devsync.spring.label.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLabelRequest {
    @NotBlank
    String name;

    @NotBlank
    @Pattern(
            regexp = "^#(?:[A-Fa-f0-9]{3}|[A-Fa-f0-9]{6})$"
    )
    private String color;

    String description;
}
