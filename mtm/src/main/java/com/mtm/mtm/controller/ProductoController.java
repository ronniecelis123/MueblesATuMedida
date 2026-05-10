package com.mtm.mtm.controller;

import com.mtm.mtm.model.Producto;
import com.mtm.mtm.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // ==========================
    // GET ALL PRODUCTOS
    // ==========================
    @GetMapping
    public List<Producto> getAll() {

        return productoRepository
                .findByActivoTrue();
    }

    // ==========================
    // GET PRODUCTO BY ID
    // ==========================
    @GetMapping("/{id}")
    public Producto getById(@PathVariable Integer id) {

        return productoRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));
    }
}