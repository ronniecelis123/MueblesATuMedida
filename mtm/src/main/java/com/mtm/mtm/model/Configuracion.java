package com.mtm.mtm.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "configuracion")


public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Integer idConfiguracion;

    private Double largo;
    private Double ancho;
    private Double alto;

    @Column(name = "precio_calculado")
    private Double precioCalculado;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "id_acabado", nullable = false)
    private Acabado acabado;

    @ManyToOne
    @JoinColumn(name = "id_tipo_pata", nullable = false)
    private TipoPata tipoPata;

    // getters/setters iguales
}