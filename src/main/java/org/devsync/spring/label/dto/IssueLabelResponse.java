package org.devsync.spring.label.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.label.entity.LabelStatus;

import java.util.UUID;

@Data
@Builder
public class IssueLabelResponse {
    private UUID id;
    private UUID labelId;
    private String name;
    private String color;
    private LabelStatus status;
}
