package com.mtm.mtm.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrdenDetalle> detalles;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    private Double total;

    private String estado;

    // GETTERS Y SETTERS

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<OrdenDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenDetalle> detalles) {
        this.detalles = detalles;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}