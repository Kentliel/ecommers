package com.ecommers.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Lo que el servidor devuelve cuando envías o pides un producto.
public class BrandResponseDto
{
    private UUID id;
    private String name;
    private String country;
    private String website;
    private Boolean active;
}
