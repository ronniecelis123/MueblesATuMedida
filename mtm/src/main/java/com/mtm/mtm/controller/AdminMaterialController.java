package com.mtm.mtm.controller;

import com.mtm.mtm.model.Material;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.MaterialRepository;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/materiales")
public class AdminMaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==========================
    // LISTAR
    // ==========================
    @GetMapping
    public String listar(Model model,
                         Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute(
                "materiales",
                materialRepository.findAll()
        );

        return "admin/materiales";
    }

    // ==========================
    // NUEVO
    // ==========================
    @GetMapping("/nuevo")
    public String nuevo(Model model,
                        Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute(
                "material",
                new Material()
        );

        return "admin/material-form";
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Material material) {

        // VALIDACIONES
        if (material.getCostoM2() == null ||
                material.getCostoM2()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            material.setCostoM2(
                    new BigDecimal("1.0")
            );
        }

        // ACTIVO DEFAULT
        if (material.getActivo() == null) {

            material.setActivo(true);
        }

        materialRepository.save(material);

        return "redirect:/admin/materiales";
    }

    // ==========================
    // EDITAR
    // ==========================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                         Model model,
                         Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName());

        Material material = materialRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute(
                "material",
                material
        );

        return "admin/material-form";
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        materialRepository.deleteById(id);

        return "redirect:/admin/materiales";
    }
}