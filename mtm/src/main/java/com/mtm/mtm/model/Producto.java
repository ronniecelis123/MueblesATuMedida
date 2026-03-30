package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    private String nombre;
    private String descripcion;

    @Column(name = "precio_base")
    private BigDecimal precioBase;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "categoria")
    private String categoria;

    private Boolean activo;
}