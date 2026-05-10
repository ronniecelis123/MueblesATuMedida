package com.mtm.mtm.repository;

import com.mtm.mtm.model.Carrito;
import com.mtm.mtm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Carrito findByUsuarioAndEstado(Usuario usuario, String estado);
}