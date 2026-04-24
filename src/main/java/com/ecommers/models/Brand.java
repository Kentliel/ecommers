package com.ecommers.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 9)
    private String nif;

    @Column(length = 100)
    private String country;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String logo;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist()
    {
        this.createdAt = LocalDateTime.now();

        if (this.active == null)
            this.active = true;
    }

    @PreUpdate
    public void preUpdate()
    {
        this.updateAt = LocalDateTime.now();
    }

}
