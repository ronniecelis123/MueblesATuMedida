package com.mtm.mtm.repository;

import com.mtm.mtm.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {
    List<Producto> findByActivoTrue();
    List<Producto> findByActivoTrueAndNombreContainingIgnoreCase(String nombre);
    List<Producto> findByActivoTrueAndCategoria(String categoria);
}