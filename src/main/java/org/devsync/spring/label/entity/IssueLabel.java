package org.devsync.spring.label.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.issue.entity.Issue;

import java.util.UUID;

@Entity
@Table(
        name = "issue_labels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_issue_label",
                        columnNames = {
                                "issue_id",
                                "label_id"
                        }
                )
        }
)
@Getter
@Setter
public class IssueLabel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    private Label label;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;
}
