package com.mtm.mtm.controller;

import com.mtm.mtm.model.Producto;
import com.mtm.mtm.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // ==========================
    // LISTAR PRODUCTOS
    // ==========================
    @GetMapping
    public String listar(Model model) {


        model.addAttribute("productos",
                productoRepository.findAll());

        return "admin/productos";
    }

    // ==========================
    // FORM NUEVO
    // ==========================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        Producto producto = new Producto();

        producto.setActivo(true);

        model.addAttribute("producto", producto);

        return "admin/producto-form";
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {

        productoRepository.save(producto);

        return "redirect:/admin/productos";
    }

    // ==========================
    // EDITAR
    // ==========================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                         Model model) {

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("producto", producto);

        return "admin/producto-form";
    }

    // ==========================
    // DESACTIVAR
    // ==========================
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Integer id) {

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow();

        producto.setActivo(false);

        productoRepository.save(producto);

        return "redirect:/admin/productos";
    }

    // ==========================
    // ACTIVAR
    // ==========================
    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Integer id) {

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow();

        producto.setActivo(true);

        productoRepository.save(producto);

        return "redirect:/admin/productos";
    }
}