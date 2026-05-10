package com.mtm.mtm.repository;

import com.mtm.mtm.model.RefreshToken;
import com.mtm.mtm.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(
            String token
    );

    void deleteByUsuario(
            Usuario usuario
    );
}