package org.devsync.spring.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MemberStatisticsResponse {

    private UUID userId;

    private String firstName;

    private String lastName;

    private long assignedIssues;

    private long completedIssues;

    private double completionRate;
}