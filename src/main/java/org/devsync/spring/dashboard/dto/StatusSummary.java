package org.devsync.spring.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusSummary {
    long todo;
    long inProgress;
    long done;
}
