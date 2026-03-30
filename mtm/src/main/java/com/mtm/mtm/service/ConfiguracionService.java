package com.mtm.mtm.service;

import com.mtm.mtm.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConfiguracionService {

    public BigDecimal calcularPrecio(
            Producto producto,
            Material material,
            Acabado acabado,
            TipoPata tipoPata,
            BigDecimal largo,
            BigDecimal ancho
    ) {

        BigDecimal area = largo.multiply(ancho);

        BigDecimal costoMaterial = area.multiply(material.getCostoM2());

        BigDecimal precio = producto.getPrecioBase()
                .add(costoMaterial)
                .multiply(acabado.getFactorPrecio())
                .multiply(tipoPata.getFactorPrecio());

        return precio;
    }
}