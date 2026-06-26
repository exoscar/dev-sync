package org.devsync.spring.watcher.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.issue.entity.Issue;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "issue_watchers", uniqueConstraints = {
        @UniqueConstraint(name = "uk_issue_watcher", columnNames = {
                "user_id", "issue_id"
        })
})
public class IssueWatcher extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY)
        private Issue issue;

        @ManyToOne(fetch = FetchType.LAZY)
        private User user;

        @Enumerated(EnumType.STRING)
        private WatcherSource source;

}
