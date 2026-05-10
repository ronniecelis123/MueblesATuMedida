package com.mtm.mtm.controller;

import com.mtm.mtm.model.TipoPata;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.TipoPataRepository;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/patas")
public class AdminPataController {

    @Autowired
    private TipoPataRepository tipoPataRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // =====================================
    // LISTAR
    // =====================================

    @GetMapping
    public String listar(Model model,
                         Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        model.addAttribute(
                "usuarioSesion",
                usuario
        );

        model.addAttribute(
                "patas",
                tipoPataRepository.findAll()
        );

        return "admin/patas";
    }


    // =====================================
    // NUEVO
    // =====================================

    @GetMapping("/nuevo")
    public String nuevo(Model model,
                        Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        model.addAttribute(
                "usuarioSesion",
                usuario
        );

        model.addAttribute(
                "pata",
                new TipoPata()
        );

        return "admin/pata-form";
    }


    // =====================================
    // GUARDAR
    // =====================================

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute TipoPata pata) {

        // VALIDAR FACTOR PRECIO

        if (pata.getFactorPrecio() == null ||
                pata.getFactorPrecio()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            pata.setFactorPrecio(
                    new BigDecimal("1.0")
            );
        }

        // VALIDAR ACTIVO

        if (pata.getActivo() == null) {

            pata.setActivo(true);
        }

        tipoPataRepository.save(pata);

        return "redirect:/admin/patas";
    }


    // =====================================
    // EDITAR
    // =====================================

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                         Model model,
                         Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        TipoPata pata = tipoPataRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute(
                "usuarioSesion",
                usuario
        );

        model.addAttribute(
                "pata",
                pata
        );

        return "admin/pata-form";
    }


    // =====================================
    // ELIMINAR
    // =====================================

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        tipoPataRepository.deleteById(id);

        return "redirect:/admin/patas";
    }

}