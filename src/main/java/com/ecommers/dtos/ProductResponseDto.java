package com.ecommers.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Lo que el servidor devuelve cuando envías o pides un producto.
public class ProductResponseDto
{
    private UUID id;
    private String sku;
    private String name;
    private  String slug;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Boolean active;

    private UUID categoryId;
    private String categoryName;

    private UUID brandId;
    private String brandName;
}
