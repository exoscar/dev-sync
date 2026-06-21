package org.devsync.spring.label.mapper;

import org.devsync.spring.label.dto.IssueLabelResponse;
import org.devsync.spring.label.entity.IssueLabel;
import org.springframework.stereotype.Component;


@Component
public class IssueLabelMapper {
    public IssueLabelResponse toResponse(IssueLabel issueLabel) {
     return  IssueLabelResponse.builder()
             .id(issueLabel.getId())
             .labelId(issueLabel.getLabel().getId())
             .name(issueLabel.getLabel().getName())
             .color(issueLabel.getLabel().getColor())
             .status(issueLabel.getLabel().getStatus())
             .build();
    }
}
