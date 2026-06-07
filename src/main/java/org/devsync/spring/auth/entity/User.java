package org.devsync.spring.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.common.entity.BaseEntity;
import org.devsync.spring.user.entity.Role;

import java.util.UUID;

@Entity
@Getter
@Setter
    @Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
