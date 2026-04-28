package com.ecommers.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Lo que el cliente manda para crear/editar un producto.
public class ProductRequestDto
{
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Boolean active;
    private UUID categoryId;
    private UUID brandId;
}
