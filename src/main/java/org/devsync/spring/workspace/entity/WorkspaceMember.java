package org.devsync.spring.workspace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.entity.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {
                        "workspace_id","user_id"
                }
        )
})
public class WorkspaceMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private WorkspaceRole role;
}
