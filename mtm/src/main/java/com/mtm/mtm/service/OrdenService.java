package com.mtm.mtm.service;

import com.mtm.mtm.model.Orden;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.OrdenRepository;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // ==========================
    // OBTENER CARRITO ACTIVO
    // ==========================
    public Orden obtenerCarritoActivo(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        Orden orden = ordenRepository
                .findByUsuarioAndEstado(usuario, "CARRITO");

        if (orden == null) {

            orden = new Orden();

            orden.setUsuario(usuario);
            orden.setEstado("CARRITO");
            orden.setFechaCreacion(new Date());
            orden.setTotal(0.0);

            orden = ordenRepository.save(orden);
        }

        return orden;
    }

    // ==========================
    // FINALIZAR COMPRA
    // ==========================
    public Orden finalizarCompra(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        Orden orden = ordenRepository
                .findByUsuarioAndEstado(usuario, "CARRITO");

        if (orden == null) {
            return null;
        }

        // VALIDAR CARRITO VACÍO
        if (orden.getDetalles() == null ||
                orden.getDetalles().isEmpty()) {

            return null;
        }

        //  CAMBIAR ESTADO
        orden.setEstado("PAGADO");

        // ACTUALIZAR FECHA
        orden.setFechaCreacion(new Date());

        return ordenRepository.save(orden);
    }
    // ==========================
    // HISTORIAL DE COMPRAS
    // ==========================
    public java.util.List<Orden> obtenerCompras(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        return ordenRepository
                .findByUsuarioAndEstadoOrderByFechaCreacionDesc(
                        usuario,
                        "PAGADO"
                );
    }

    // ==========================
    // OBTENER ORDEN
    // ==========================
    public Orden obtenerOrden(Long idOrden) {

        return ordenRepository
                .findById(idOrden)
                .orElseThrow();
    }




}