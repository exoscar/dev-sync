package org.devsync.spring.attachment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.issue.entity.Issue;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(
                        name = "idx_attachment_issue",
                        columnList = "issue_id"
                )
        }
)
@Getter
@Setter
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false)
    private User uploadedBy;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String storedFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

}
