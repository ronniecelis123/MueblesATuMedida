package com.mtm.mtm.controller;

import com.mtm.mtm.model.Acabado;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.AcabadoRepository;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/acabados")
public class AdminAcabadoController {

    @Autowired
    private AcabadoRepository acabadoRepository;

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
                "acabados",
                acabadoRepository.findAll()
        );

        return "admin/acabados";
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
                "acabado",
                new Acabado()
        );

        return "admin/acabado-form";
    }

    // ==========================
    // GUARDAR
    // ==========================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Acabado acabado) {

        // VALIDAR FACTOR
        if (acabado.getFactorPrecio() == null ||
                acabado.getFactorPrecio()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            acabado.setFactorPrecio(
                    new BigDecimal("1.0")
            );
        }

        // ACTIVO DEFAULT
        if (acabado.getActivo() == null) {

            acabado.setActivo(true);
        }

        acabadoRepository.save(acabado);

        return "redirect:/admin/acabados";
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

        Acabado acabado = acabadoRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute(
                "acabado",
                acabado
        );

        return "admin/acabado-form";
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        acabadoRepository.deleteById(id);

        return "redirect:/admin/acabados";
    }
}