package com.mtm.mtm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;


    // ==========================
    // DATOS PRINCIPALES
    // ==========================
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_base")
    private BigDecimal precioBase;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "base3d")
    private String base3d;

    @Column(name = "patas3d")
    private String patas3d;
    @Column(name = "modelo_path")
    private String modeloPath;
    private String categoria;

    private Boolean activo;

    // ==========================
    // RELACIONES
    // ==========================
    @JsonIgnore
    @OneToMany(mappedBy = "producto")
    private List<Configuracion> configuraciones;
}