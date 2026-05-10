package com.mtm.mtm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;

    //  RELACIÓN CON USUARIO
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String estado;

    // =====================
    // GETTERS & SETTERS
    // =====================

    public Integer getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}