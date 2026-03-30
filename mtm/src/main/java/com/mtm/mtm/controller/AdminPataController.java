package com.mtm.mtm.controller;

import com.mtm.mtm.model.TipoPata;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.TipoPataRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/patas")
public class AdminPataController {

    @Autowired
    private TipoPataRepository tipoPataRepository;

    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("patas", tipoPataRepository.findAll());

        return "admin/patas";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("pata", new TipoPata());

        return "admin/pata-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute TipoPata pata) {

        if (pata.getFactorPrecio() == null ||
                pata.getFactorPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            pata.setFactorPrecio(new BigDecimal("1.0"));
        }

        if (pata.getActivo() == null) {
            pata.setActivo(true);
        }

        tipoPataRepository.save(pata);

        return "redirect:/admin/patas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("pata", tipoPataRepository.findById(id).orElseThrow());

        return "admin/pata-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        tipoPataRepository.deleteById(id);

        return "redirect:/admin/patas";
    }
}