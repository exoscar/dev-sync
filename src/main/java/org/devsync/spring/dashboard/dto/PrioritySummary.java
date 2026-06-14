package org.devsync.spring.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrioritySummary {
    long low;
    long medium;
    long high;
    long critical;
}
