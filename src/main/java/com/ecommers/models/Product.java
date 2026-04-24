package com.ecommers.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "Productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Delegar el ID a la Base de Datos.
    private Long id;

    @Column(unique = true) //El titulos del producto sea tipo Unico
    String  Title;

    Double Price; //Precio del producto

    String shortDescription; //Descripción corta del producto

    String longDescription; //Descripción larga del producto

    Integer Stock; // Cantidad de productos disponibles en el inventario

    String imageUrl; //URL de la imagen del producto

}
