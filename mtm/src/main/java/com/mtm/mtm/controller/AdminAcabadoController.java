package com.mtm.mtm.controller;

import com.mtm.mtm.model.Acabado;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.AcabadoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/acabados")
public class AdminAcabadoController {

    @Autowired
    private AcabadoRepository acabadoRepository;

    // LISTAR
    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("acabados", acabadoRepository.findAll());

        return "admin/acabados";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        model.addAttribute("acabado", new Acabado());

        return "admin/acabado-form";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Acabado acabado) {

        //  VALIDACIÓN IMPORTANTE
        if (acabado.getFactorPrecio() == null ||
                acabado.getFactorPrecio().compareTo(BigDecimal.ZERO) <= 0) {

            acabado.setFactorPrecio(new BigDecimal("1.0"));
        }

        if (acabado.getActivo() == null) {
            acabado.setActivo(true);
        }

        acabadoRepository.save(acabado);

        return "redirect:/admin/acabados";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuarioSesion", usuario);

        Acabado acabado = acabadoRepository.findById(id).orElseThrow();
        model.addAttribute("acabado", acabado);

        return "admin/acabado-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        acabadoRepository.deleteById(id);

        return "redirect:/admin/acabados";
    }
}