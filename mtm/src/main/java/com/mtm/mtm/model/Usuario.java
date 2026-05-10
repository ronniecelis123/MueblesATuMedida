package com.mtm.mtm.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(unique = true)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo válido")
    private String email;

    @Column(name = "password_hash")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;
}