package org.devsync.spring.issue.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.project.entity.Project;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Issue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;


}
