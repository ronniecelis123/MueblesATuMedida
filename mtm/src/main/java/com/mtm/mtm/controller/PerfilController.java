package com.mtm.mtm.controller;

import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.UsuarioRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==========================
    // VER PERFIL
    // ==========================
    @GetMapping
    public String perfil(Authentication auth,
                         Model model) {

        Usuario usuario = usuarioRepository
                .findByEmail(auth.getName());

        model.addAttribute("usuario", usuario);

        model.addAttribute("usuarioSesion", usuario);

        return "perfil";
    }

    // ==========================
    // ACTUALIZAR NOMBRE
    // ==========================
    @PostMapping("/actualizar")
    public String actualizar(@RequestParam String nombre,
                             Authentication auth) {

        Usuario usuario = usuarioRepository
                .findByEmail(auth.getName());

        usuario.setNombre(nombre);

        usuarioRepository.save(usuario);

        return "redirect:/?nombreActualizado";
    }

    // ==========================
    // ELIMINAR CUENTA
    // ==========================
    @PostMapping("/eliminar")
    public String eliminar(Authentication auth,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        Usuario usuario = usuarioRepository
                .findByEmail(auth.getName());

        if (usuario.getRol().getNombre().equals("ADMIN")) {

            return "redirect:/perfil?error=admin";
        }

        usuario.setActivo(false);

        usuarioRepository.save(usuario);

        request.getSession().invalidate();

        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/?cuentaEliminada";
    }
}