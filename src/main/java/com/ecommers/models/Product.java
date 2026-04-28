package com.ecommers.models;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku; // Stock Keeping Unit (Unidad de Mantenimiento de Existencias)

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug; // parte final de una URL que identifica de forma legible una página.

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean active;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private  LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @PrePersist
    public void prePersist()
    {
        this.createdAt = LocalDateTime.now();

        if (this.active == null)
            this.active = true;

        if (this.stock == null)
            this.stock = 0;
    }

    @PreUpdate
    public void preUpdate()
    {
        this.updateAt = LocalDateTime.now();
    }
}



