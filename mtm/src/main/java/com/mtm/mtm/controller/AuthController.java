package com.mtm.mtm.controller;

import com.mtm.mtm.model.RefreshToken;
import com.mtm.mtm.model.Rol;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.RolRepository;
import com.mtm.mtm.repository.UsuarioRepository;
import com.mtm.mtm.service.RefreshTokenService;
import com.mtm.mtm.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==========================
    // REGISTRO
    // ==========================
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model
    ) {

        // VALIDACIONES
        if (result.hasErrors()) {
            return "registro";
        }

        // EMAIL DUPLICADO
        if (usuarioRepository.findByEmail(
                usuario.getEmail()) != null) {

            model.addAttribute(
                    "error",
                    "El correo ya está registrado"
            );

            return "registro";
        }

        // CAMPOS VACÍOS
        if (usuario.getNombre().isEmpty() ||
                usuario.getEmail().isEmpty() ||
                usuario.getPassword().isEmpty()) {

            model.addAttribute(
                    "error",
                    "Todos los campos son obligatorios"
            );

            return "registro";
        }

        Rol rolUsuario =
                rolRepository.findByNombre("USUARIO")
                        .orElseThrow();

        usuario.setRol(rolUsuario);

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()
                )
        );

        usuarioRepository.save(usuario);

        return "redirect:/login?success";
    }

    // ==========================
    // LOGIN VIEW
    // ==========================
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    // ==========================
    // LOGIN
    // ==========================
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {

        Usuario usuario =
                usuarioRepository.findByEmail(email);

        if (usuario == null ||
                !passwordEncoder.matches(
                        password,
                        usuario.getPassword()
                )) {

            return "redirect:/login?error";
        }

        // ==========================
        // GENERAR JWT
        // ==========================
        String jwt =
                jwtUtil.generateToken(
                        usuario.getEmail()
                );

        Cookie jwtCookie =
                new Cookie("JWT", jwt);

        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        response.addCookie(jwtCookie);

        // ==========================
        // GENERAR REFRESH TOKEN
        // ==========================
        RefreshToken refreshToken =
                refreshTokenService
                        .createToken(usuario);

        Cookie refreshCookie =
                new Cookie(
                        "REFRESH_TOKEN",
                        refreshToken.getToken()
                );

        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24);

        response.addCookie(refreshCookie);

        // ==========================
        // REDIRECCIÓN
        // ==========================
        if (usuario.getRol()
                .getNombre()
                .equals("ADMIN")) {

            return "redirect:/admin";
        }

        return "redirect:/";
    }

    // ==========================
    // REFRESH JWT
    // ==========================
    @PostMapping("/auth/refresh")
    @ResponseBody
    public String refreshToken(

            @CookieValue(
                    name = "REFRESH_TOKEN",
                    required = false
            )
            String refreshTokenValue

    ) {

        if (refreshTokenValue == null) {

            throw new RuntimeException(
                    "Refresh token no encontrado"
            );
        }

        RefreshToken refreshToken =
                refreshTokenService
                        .findByToken(
                                refreshTokenValue
                        );

        if (refreshToken == null) {

            throw new RuntimeException(
                    "Refresh token inválido"
            );
        }

        // ==========================
        // VALIDAR EXPIRACIÓN
        // ==========================
        if (refreshToken
                .getExpiryDate()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expirado"
            );
        }

        // ==========================
        // NUEVO JWT
        // ==========================
        return jwtUtil.generateToken(
                refreshToken
                        .getUsuario()
                        .getEmail()
        );
    }

    // ==========================
    // LOGOUT
    // ==========================
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie jwtCookie =
                new Cookie("JWT", "");

        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);

        Cookie refreshCookie =
                new Cookie("REFRESH_TOKEN", "");

        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(refreshCookie);

        SecurityContextHolder.clearContext();

        return "redirect:/login?logout";
    }
}