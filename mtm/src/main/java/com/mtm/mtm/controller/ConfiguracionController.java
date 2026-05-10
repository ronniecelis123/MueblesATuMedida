package com.mtm.mtm.controller;

import com.mtm.mtm.dto.ConfiguracionDTO;
import com.mtm.mtm.model.*;
import com.mtm.mtm.repository.*;
import com.mtm.mtm.service.ConfiguracionCrudService;
import com.mtm.mtm.service.ConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/configuraciones")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionCrudService crudService;

    @Autowired
    private ConfiguracionService calculoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private AcabadoRepository acabadoRepository;

    @Autowired
    private TipoPataRepository tipoPataRepository;

    // ==========================
    // GET ALL
    // ==========================
    @GetMapping
    public List<ConfiguracionDTO> getAll() {

        return crudService.getAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // ==========================
    // GET BY ID
    // ==========================
    @GetMapping("/{id}")
    public ConfiguracionDTO getById(@PathVariable Integer id) {

        Configuracion configuracion =
                crudService.getById(id);

        return convertirDTO(configuracion);
    }

    // ==========================
    // CREATE
    // ==========================
    @PostMapping
    public ConfiguracionDTO create(@RequestBody Configuracion c,
                                   Authentication authentication) {

        String email = authentication.getName();

        Usuario usuario =
                usuarioRepository.findByEmail(email);

        // ==========================
        // VALIDACIONES
        // ==========================
        if (c.getLargo() <= 0 || c.getAncho() <= 0) {

            throw new RuntimeException(
                    "Las dimensiones deben ser mayores a 0"
            );
        }

        if (c.getLargo() > 500 || c.getAncho() > 500) {

            throw new RuntimeException(
                    "Dimensiones demasiado grandes"
            );
        }

        // ==========================
        // BUSCAR RELACIONES
        // ==========================
        Producto producto =
                productoRepository.findById(
                        c.getProducto().getIdProducto()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado"
                        )
                );

        Material material =
                materialRepository.findById(
                        c.getMaterial().getIdMaterial()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Material no encontrado"
                        )
                );

        Acabado acabado =
                acabadoRepository.findById(
                        c.getAcabado().getIdAcabado()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Acabado no encontrado"
                        )
                );

        TipoPata tipoPata =
                tipoPataRepository.findById(
                        c.getTipoPata().getIdTipoPata()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Tipo de pata no encontrado"
                        )
                );

        // ==========================
        // SET RELACIONES
        // ==========================
        c.setUsuario(usuario);
        c.setProducto(producto);
        c.setMaterial(material);
        c.setAcabado(acabado);
        c.setTipoPata(tipoPata);

        // ==========================
        // CALCULAR PRECIO
        // ==========================
        BigDecimal largoM =
                BigDecimal.valueOf(c.getLargo())
                        .divide(
                                new BigDecimal("100"),
                                4,
                                RoundingMode.HALF_UP
                        );

        BigDecimal anchoM =
                BigDecimal.valueOf(c.getAncho())
                        .divide(
                                new BigDecimal("100"),
                                4,
                                RoundingMode.HALF_UP
                        );

        BigDecimal precio =
                calculoService.calcularPrecio(
                        producto,
                        material,
                        acabado,
                        tipoPata,
                        largoM,
                        anchoM
                );

        c.setPrecioCalculado(
                precio.setScale(
                        2,
                        RoundingMode.HALF_UP
                ).doubleValue()
        );

        Configuracion guardada =
                crudService.save(c);

        return convertirDTO(guardada);
    }

    // ==========================
    // DELETE
    // ==========================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {

        crudService.delete(id);
    }

    // ==========================
    // CALCULAR PRECIO
    // ==========================
    @PostMapping("/calcular")
    public BigDecimal calcular(@RequestBody Configuracion c) {

        Producto producto =
                productoRepository.findById(
                        c.getProducto().getIdProducto()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado"
                        )
                );

        Material material =
                materialRepository.findById(
                        c.getMaterial().getIdMaterial()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Material no encontrado"
                        )
                );

        Acabado acabado =
                acabadoRepository.findById(
                        c.getAcabado().getIdAcabado()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Acabado no encontrado"
                        )
                );

        TipoPata tipoPata =
                tipoPataRepository.findById(
                        c.getTipoPata().getIdTipoPata()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Tipo de pata no encontrado"
                        )
                );

        BigDecimal largoM =
                BigDecimal.valueOf(c.getLargo())
                        .divide(
                                new BigDecimal("100"),
                                4,
                                RoundingMode.HALF_UP
                        );

        BigDecimal anchoM =
                BigDecimal.valueOf(c.getAncho())
                        .divide(
                                new BigDecimal("100"),
                                4,
                                RoundingMode.HALF_UP
                        );

        BigDecimal precio =
                calculoService.calcularPrecio(
                        producto,
                        material,
                        acabado,
                        tipoPata,
                        largoM,
                        anchoM
                );

        return precio.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    // ==========================
    // CONVERTIR DTO
    // ==========================
    private ConfiguracionDTO convertirDTO(Configuracion c) {

        ConfiguracionDTO dto =
                new ConfiguracionDTO();

        dto.setIdConfiguracion(
                c.getIdConfiguracion()
        );

        dto.setLargo(c.getLargo());
        dto.setAncho(c.getAncho());
        dto.setAlto(c.getAlto());

        dto.setPrecioCalculado(
                c.getPrecioCalculado()
        );

        dto.setProducto(
                c.getProducto().getNombre()
        );

        dto.setMaterial(
                c.getMaterial().getNombre()
        );

        dto.setAcabado(
                c.getAcabado().getNombre()
        );

        dto.setTipoPata(
                c.getTipoPata().getNombre()
        );

        return dto;
    }
}