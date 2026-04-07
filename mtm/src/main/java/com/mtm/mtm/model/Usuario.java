package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "usuario")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nombre;

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;
}