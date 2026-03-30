package com.mtm.mtm.controller;

import com.mtm.mtm.model.Rol;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.RolRepository;
import com.mtm.mtm.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @ModelAttribute Usuario usuario,
            Model model
    ) {

        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro";
        }

        if (usuario.getNombre().isEmpty() ||
                usuario.getEmail().isEmpty() ||
                usuario.getPassword().isEmpty()) {

            model.addAttribute("error", "Todos los campos son obligatorios");
            return "registro";
        }

        Rol rolUsuario = rolRepository.findByNombre("USUARIO").orElseThrow();
        usuario.setRol(rolUsuario);

        usuarioRepository.save(usuario);

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            return "redirect:/login?error";
        }

        session.setAttribute("usuario", usuario);

        if (usuario.getRol().getNombre().equals("ADMIN")) {
            return "redirect:/admin";
        }

        return "redirect:/";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
