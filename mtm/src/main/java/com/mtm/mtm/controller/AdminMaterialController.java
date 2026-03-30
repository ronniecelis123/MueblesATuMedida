package com.mtm.mtm.controller;

import com.mtm.mtm.model.Material;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.MaterialRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/materiales")
public class AdminMaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("materiales", materialRepository.findAll());

        return "admin/materiales";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("material", new Material());

        return "admin/material-form";
    }
    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Material material) {
        if (material.getCostoM2() == null ||
                material.getCostoM2().compareTo(BigDecimal.ZERO) <= 0) {

            material.setCostoM2(new BigDecimal("1.0"));
        }

        if (material.getActivo() == null) {
            material.setActivo(true);
        }

        materialRepository.save(material);

        return "redirect:/admin/materiales";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        Material material = materialRepository.findById(id).orElseThrow();
        model.addAttribute("material", material);

        return "admin/material-form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        materialRepository.deleteById(id);
        return "redirect:/admin/materiales";
    }
}