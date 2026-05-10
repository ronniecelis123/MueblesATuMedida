package com.mtm.mtm.service;

import com.mtm.mtm.model.RefreshToken;
import com.mtm.mtm.model.Usuario;
import com.mtm.mtm.repository.RefreshTokenRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(
            RefreshTokenRepository repository
    ) {

        this.repository = repository;
    }

    // ==========================
    // CREAR TOKEN
    // ==========================
    public RefreshToken createToken(
            Usuario usuario
    ) {

        RefreshToken token =
                new RefreshToken();

        token.setUsuario(usuario);

        token.setToken(
                UUID.randomUUID().toString()
        );

        token.setExpiryDate(
                Instant.now().plusSeconds(86400)
        );

        return repository.save(token);
    }

    // ==========================
    // BUSCAR TOKEN
    // ==========================
    public RefreshToken findByToken(
            String token
    ) {

        return repository
                .findByToken(token)
                .orElse(null);
    }
}