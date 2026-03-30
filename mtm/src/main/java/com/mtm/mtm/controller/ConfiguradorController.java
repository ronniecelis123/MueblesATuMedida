package com.mtm.mtm.controller;

import com.mtm.mtm.model.*;
import com.mtm.mtm.repository.*;
import com.mtm.mtm.service.ConfiguracionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
public class ConfiguradorController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private AcabadoRepository acabadoRepository;

    @Autowired
    private TipoPataRepository tipoPataRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ConfiguracionService configuracionService;
    @GetMapping("/")
    public String catalogo(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoria,
            Model model,
            HttpSession session
    ) {

        // USUARIO EN SESIÓN
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        //PRODUCTOS
        List<Producto> productos;

        if (search != null && !search.isEmpty()) {
            productos = productoRepository.findByNombreContainingIgnoreCase(search);
        } else if (categoria != null && !categoria.isEmpty()) {
            productos = productoRepository.findByCategoria(categoria);
        } else {
            productos = productoRepository.findAll();
        }

        model.addAttribute("productos", productos);

        return "catalogo";
    }


    private String recargarConfigurador(Integer productoId, Model model) {

        Producto producto = productoRepository.findById(productoId).orElseThrow();

        model.addAttribute("producto", producto);
        model.addAttribute("materiales", materialRepository.findAll());
        model.addAttribute("acabados", acabadoRepository.findAll());
        model.addAttribute("patas", tipoPataRepository.findAll());

        return "configurador";
    }

    @PostMapping("/calcular")
    public String calcularPrecio(
            @RequestParam Integer productoId,
            @RequestParam Integer materialId,
            @RequestParam Integer acabadoId,
            @RequestParam Integer pataId,
            @RequestParam BigDecimal largo,
            @RequestParam BigDecimal ancho,
            Model model,
            HttpSession session
    ) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        // VALIDACIONES
        if (largo.compareTo(BigDecimal.ZERO) <= 0 ||
                ancho.compareTo(BigDecimal.ZERO) <= 0) {

            model.addAttribute("error", "Las dimensiones deben ser mayores a 0");
            return recargarConfigurador(productoId, model);
        }

        if (largo.compareTo(new BigDecimal("500")) > 0 ||
                ancho.compareTo(new BigDecimal("500")) > 0) {

            model.addAttribute("error", "Dimensiones demasiado grandes");
            return recargarConfigurador(productoId, model);
        }

        // Convertir
        BigDecimal largoM = largo.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal anchoM = ancho.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        Producto producto = productoRepository.findById(productoId).orElseThrow();
        Material material = materialRepository.findById(materialId).orElseThrow();
        Acabado acabado = acabadoRepository.findById(acabadoId).orElseThrow();
        TipoPata pata = tipoPataRepository.findById(pataId).orElseThrow();

        BigDecimal precio = configuracionService.calcularPrecio(
                producto, material, acabado, pata, largoM, anchoM
        );
        precio = precio.setScale(2, RoundingMode.HALF_UP);
        model.addAttribute("precio", precio);
        // Recargar datos
        model.addAttribute("producto", producto);
        model.addAttribute("materiales", materialRepository.findAll());
        model.addAttribute("acabados", acabadoRepository.findAll());
        model.addAttribute("patas", tipoPataRepository.findAll());

        model.addAttribute("precio", precio);

        return "configurador";
    }


    @GetMapping("/configurar/{id}")
    public String configurar(@PathVariable Integer id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        Producto producto = productoRepository.findById(id).orElseThrow();

        model.addAttribute("producto", producto);
        model.addAttribute("materiales", materialRepository.findAll());
        model.addAttribute("acabados", acabadoRepository.findAll());
        model.addAttribute("patas", tipoPataRepository.findAll());
        System.out.println("ID: " + producto.getIdProducto());
        System.out.println("NOMBRE: " + producto.getNombre());
        System.out.println("PRECIO BASE: " + producto.getPrecioBase());

        return "configurador";
    }
}