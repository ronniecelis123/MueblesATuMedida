package com.mtm.mtm.security;

import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.UsuarioRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByEmailAndActivoTrue(email);

        if (usuario == null) {

            throw new UsernameNotFoundException(
                    "Usuario no encontrado o inactivo"
            );
        }

        // ROLE_ADMIN / ROLE_USER
        String role = "ROLE_" + usuario.getRol().getNombre();

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}