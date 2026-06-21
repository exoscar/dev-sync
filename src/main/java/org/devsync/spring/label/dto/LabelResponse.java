package org.devsync.spring.label.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.label.entity.LabelStatus;

import java.util.UUID;

@Data
@Builder
public class LabelResponse {
    UUID id;

    String name;

    String color;

    String description;

    LabelStatus status;
}
