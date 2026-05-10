package com.mtm.mtm.dto;

import lombok.Data;

@Data
public class ConfiguracionDTO {

    private Integer idConfiguracion;

    private Double largo;
    private Double ancho;
    private Double alto;

    private Double precioCalculado;

    // DATOS SIMPLES
    private String producto;
    private String material;
    private String acabado;
    private String tipoPata;
}