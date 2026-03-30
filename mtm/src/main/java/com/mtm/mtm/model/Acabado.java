package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "acabado")
public class Acabado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acabado")
    private Integer idAcabado;

    private String nombre;

    @Column(name = "factor_precio")
    private BigDecimal factorPrecio;

    private Boolean activo;
}