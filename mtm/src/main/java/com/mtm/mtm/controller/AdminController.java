package com.mtm.mtm.controller;

import com.mtm.mtm.model.Usuario;

import com.mtm.mtm.repository.OrdenDetalleRepository;
import com.mtm.mtm.repository.OrdenRepository;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrdenDetalleRepository ordenDetalleRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @GetMapping("/admin")
    public String dashboard(Authentication authentication,
                            Model model) {

        // ==========================
        // USUARIO LOGUEADO
        // ==========================
        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        model.addAttribute("usuarioSesion", usuario);

        // ==========================
        // TOTAL VENTAS
        // ==========================
        Double totalVentas =
                ordenDetalleRepository.totalVentas();

        model.addAttribute(
                "totalVentas",
                totalVentas
        );

        // ==========================
        // TOTAL ORDENES
        // ==========================
        Long totalOrdenes =
                ordenRepository.count();

        model.addAttribute(
                "totalOrdenes",
                totalOrdenes
        );

        // ==========================
        // TOTAL PRODUCTOS VENDIDOS
        // ==========================
        Long productosVendidos =
                ordenDetalleRepository
                        .totalProductosVendidos();

        model.addAttribute(
                "productosVendidos",
                productosVendidos
        );

        // ==========================
        // PRODUCTOS MÁS VENDIDOS
        // ==========================
        List<Object[]> productos =
                ordenDetalleRepository
                        .productosMasVendidos();

        List<String> nombres = new ArrayList<>();
        List<Long> cantidades = new ArrayList<>();

        for (Object[] fila : productos) {

            nombres.add((String) fila[0]);

            cantidades.add(
                    ((Long) fila[1])
            );
        }

        model.addAttribute(
                "productosLabels",
                nombres
        );

        model.addAttribute(
                "productosData",
                cantidades
        );
        // ==========================
// ORDENES POR ESTADO
// ==========================
        List<Object[]> estados =
                ordenRepository.ordenesPorEstado();

        List<String> estadosLabels =
                new ArrayList<>();

        List<Long> estadosData =
                new ArrayList<>();

        for (Object[] fila : estados) {

            estadosLabels.add(
                    (String) fila[0]
            );

            estadosData.add(
                    ((Long) fila[1])
            );
        }

        model.addAttribute(
                "estadosLabels",
                estadosLabels
        );

        model.addAttribute(
                "estadosData",
                estadosData
        );

        return "admin/dashboard";
    }
}