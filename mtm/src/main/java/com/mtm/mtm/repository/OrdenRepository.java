package com.mtm.mtm.repository;

import com.mtm.mtm.model.Orden;
import com.mtm.mtm.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdenRepository
        extends JpaRepository<Orden, Long> {

    Orden findByUsuarioAndEstado(
            Usuario usuario,
            String estado
    );

    List<Orden> findByUsuarioAndEstadoOrderByFechaCreacionDesc(
            Usuario usuario,
            String estado
    );

    // ==========================
    // TOTAL ORDENES PAGADAS
    // ==========================
    @Query("""
        SELECT COUNT(o)
        FROM Orden o
        WHERE o.estado = 'PAGADO'
    """)
    Long totalOrdenesPagadas();

    // ==========================
    // VENTAS POR ESTADO
    // ==========================
    @Query("""
        SELECT o.estado, COUNT(o)
        FROM Orden o
        GROUP BY o.estado
    """)
    List<Object[]> ordenesPorEstado();
}