package org.devsync.spring.label.mapper;

import org.devsync.spring.label.dto.LabelResponse;
import org.devsync.spring.label.entity.Label;
import org.springframework.stereotype.Component;

@Component
public class LabelMapper {
    public LabelResponse toResponse(Label label){
        return LabelResponse.builder()
                .id(label.getId())
                .name(label.getName())
                .color(label.getColor())
                .description(label.getDescription())
                .status(label.getStatus())
                .build();
    }
}
