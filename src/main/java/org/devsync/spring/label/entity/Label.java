package org.devsync.spring.label.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Table(
        name = "labels",
        uniqueConstraints = {
                @UniqueConstraint(  name = "uk_label_workspace_name",
                        columnNames = {
                                "workspace_id",
                                "normalized_name"
                        })
        }
)
@Setter
@Getter
public class Label extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String normalizedName;

    @Column(nullable = false, length = 20)
    private String color;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LabelStatus status;

}
