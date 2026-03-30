package com.mtm.mtm.controller;

import com.mtm.mtm.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPanel(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !usuario.getRol().getNombre().equals("ADMIN")) {
            return "redirect:/";
        }

        model.addAttribute("usuarioSesion", usuario);

        return "admin/dashboard";
    }
}