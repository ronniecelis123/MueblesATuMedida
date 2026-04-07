package com.mtm.mtm.controller;

import com.mtm.mtm.model.*;
import com.mtm.mtm.repository.*;
import com.mtm.mtm.service.ConfiguracionCrudService;
import com.mtm.mtm.service.ConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/configuraciones")
public class ConfiguracionController {

    @Autowired private ConfiguracionCrudService crudService;
    @Autowired private ConfiguracionService calculoService;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private MaterialRepository materialRepository;
    @Autowired private AcabadoRepository acabadoRepository;
    @Autowired private TipoPataRepository tipoPataRepository;

    @GetMapping
    public List<Configuracion> getAll() {
        return crudService.getAll();
    }

    @PostMapping
    public Configuracion create(@RequestBody Configuracion c) {

        if (c.getLargo() <= 0 || c.getAncho() <= 0) {
            throw new RuntimeException("Las dimensiones deben ser mayores a 0");
        }

        if (c.getLargo() > 500 || c.getAncho() > 500) {
            throw new RuntimeException("Dimensiones demasiado grandes");
        }

        if (c.getUsuario() == null) {
            throw new RuntimeException("Debe enviar usuario");
        }

        Usuario usuario = usuarioRepository.findById(c.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(c.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Material material = materialRepository.findById(c.getMaterial().getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        Acabado acabado = acabadoRepository.findById(c.getAcabado().getIdAcabado())
                .orElseThrow(() -> new RuntimeException("Acabado no encontrado"));

        TipoPata tipoPata = tipoPataRepository.findById(c.getTipoPata().getIdTipoPata())
                .orElseThrow(() -> new RuntimeException("Tipo de pata no encontrado"));

        c.setUsuario(usuario);
        c.setProducto(producto);
        c.setMaterial(material);
        c.setAcabado(acabado);
        c.setTipoPata(tipoPata);

        BigDecimal largoM = BigDecimal.valueOf(c.getLargo())
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal anchoM = BigDecimal.valueOf(c.getAncho())
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal precio = calculoService.calcularPrecio(
                producto,
                material,
                acabado,
                tipoPata,
                largoM,
                anchoM
        );

        c.setPrecioCalculado(precio.setScale(2, RoundingMode.HALF_UP).doubleValue());

        return crudService.save(c);
    }

    @PutMapping("/{id}")
    public Configuracion update(@PathVariable Integer id, @RequestBody Configuracion c) {

        Configuracion existente = crudService.getById(id);


        if (c.getUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(c.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            existente.setUsuario(usuario);
        }

        if (c.getProducto() != null) {
            Producto producto = productoRepository.findById(c.getProducto().getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        if (c.getMaterial() != null) {
            Material material = materialRepository.findById(c.getMaterial().getIdMaterial())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado"));
            existente.setMaterial(material);
        }

        if (c.getAcabado() != null) {
            Acabado acabado = acabadoRepository.findById(c.getAcabado().getIdAcabado())
                    .orElseThrow(() -> new RuntimeException("Acabado no encontrado"));
            existente.setAcabado(acabado);
        }

        if (c.getTipoPata() != null) {
            TipoPata tipoPata = tipoPataRepository.findById(c.getTipoPata().getIdTipoPata())
                    .orElseThrow(() -> new RuntimeException("Tipo de pata no encontrado"));
            existente.setTipoPata(tipoPata);
        }

        if (c.getLargo() != null) existente.setLargo(c.getLargo());
        if (c.getAncho() != null) existente.setAncho(c.getAncho());
        if (c.getAlto() != null) existente.setAlto(c.getAlto());

        if (existente.getLargo() != null && existente.getAncho() != null) {

            BigDecimal largoM = BigDecimal.valueOf(existente.getLargo())
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal anchoM = BigDecimal.valueOf(existente.getAncho())
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal precio = calculoService.calcularPrecio(
                    existente.getProducto(),
                    existente.getMaterial(),
                    existente.getAcabado(),
                    existente.getTipoPata(),
                    largoM,
                    anchoM
            );

            existente.setPrecioCalculado(precio.doubleValue());
        }

        return crudService.save(existente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        crudService.delete(id);
    }

    @GetMapping("/{id}")
    public Configuracion getById(@PathVariable Integer id) {
        return crudService.getById(id);
    }
    @PostMapping("/calcular")
    public BigDecimal calcular(@RequestBody Configuracion c) {

        Producto producto = productoRepository.findById(c.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Material material = materialRepository.findById(c.getMaterial().getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        Acabado acabado = acabadoRepository.findById(c.getAcabado().getIdAcabado())
                .orElseThrow(() -> new RuntimeException("Acabado no encontrado"));

        TipoPata tipoPata = tipoPataRepository.findById(c.getTipoPata().getIdTipoPata())
                .orElseThrow(() -> new RuntimeException("Tipo de pata no encontrado"));

        BigDecimal largoM = BigDecimal.valueOf(c.getLargo())
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal anchoM = BigDecimal.valueOf(c.getAncho())
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);


        BigDecimal precio = calculoService.calcularPrecio(
                producto,
                material,
                acabado,
                tipoPata,
                largoM,
                anchoM
        );

        return precio.setScale(2, RoundingMode.HALF_UP);
    }
}