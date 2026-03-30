package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Integer idMaterial;

    private String nombre;

    @Column(name = "costo_m2")
    private BigDecimal costoM2;

    private Boolean activo;
}