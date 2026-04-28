package com.ecommers.models;

import com.ecommers.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    public void prePersist()
    {
        this.enabled = true;
        this.createdAt = LocalDateTime.now();

        if(this.role == null)
            this.role = Role.CUSTOMER;
    }

    @PreUpdate
    public void preUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }
}
