package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tipo_pata")
public class TipoPata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pata")
    private Integer idTipoPata;

    private String nombre;

    @Column(name = "factor_precio")
    private BigDecimal factorPrecio;

    private Boolean activo;
}