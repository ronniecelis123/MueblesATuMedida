package com.mtm.mtm.repository;

import com.mtm.mtm.model.Orden;
import com.mtm.mtm.model.OrdenDetalle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdenDetalleRepository
        extends JpaRepository<OrdenDetalle, Integer> {

    List<OrdenDetalle> findByOrden(Orden orden);

    // ==========================
    // TOTAL CARRITO
    // ==========================
    @Query("""
            SELECT COALESCE(SUM(o.subtotal),0)
            FROM OrdenDetalle o
            WHERE o.orden.idOrden = :ordenId
            """)
    Double calcularTotal(@Param("ordenId") Long ordenId);

    // ==========================
    // TOTAL VENTAS
    // ==========================
    @Query("""
            SELECT COALESCE(SUM(o.subtotal),0)
            FROM OrdenDetalle o
            """)
    Double totalVentas();

    // ==========================
    // TOTAL PRODUCTOS VENDIDOS
    // ==========================
    @Query("""
            SELECT COUNT(o)
            FROM OrdenDetalle o
            """)
    Long totalProductosVendidos();

    // ==========================
    // PRODUCTOS MÁS VENDIDOS
    // ==========================
    @Query("""
        SELECT 
        o.configuracion.producto.nombre,
        COUNT(o)

        FROM OrdenDetalle o

        GROUP BY o.configuracion.producto.nombre

        ORDER BY COUNT(o) DESC
    """)
    List<Object[]> productosMasVendidos();
}