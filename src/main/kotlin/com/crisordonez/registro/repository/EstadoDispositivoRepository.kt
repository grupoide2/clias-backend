package com.crisordonez.registro.repository

import com.crisordonez.registro.model.dtos.EstadoDispositivoDto
import com.crisordonez.registro.model.entities.CodigoQREntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface EstadoDispositivoRepository : PagingAndSortingRepository<CodigoQREntity, String> {

    @Query(
"""
        SELECT new com.crisordonez.registro.model.dtos.EstadoDispositivoDto(
            cq.codigo,
            CASE
                WHEN ev.fechaResultado IS NOT NULL THEN 'Resultado Listo'
                WHEN ev.fechaExamen IS NOT NULL THEN 'En Proceso'
                WHEN dr.fechaRegistro IS NOT NULL THEN 'Registrado'
                ELSE 'Generado'
            END AS estado,
            dr.fechaRegistro,
            ev.fechaExamen,
            ev.fechaResultado,
            cq.fechaExpiracion
        )
        FROM CodigoQREntity cq
        LEFT JOIN DispositivoRegistradoEntity dr ON cq.codigo = dr.dispositivo
        LEFT JOIN ExamenVphEntity ev ON cq.codigo = ev.dispositivo
        ORDER BY cq.codigo ASC
    """
        )
        fun findEstadoDispositivosSinFiltro(pageable: Pageable): Page<EstadoDispositivoDto>

    @Query(
        """
        SELECT new com.crisordonez.registro.model.dtos.EstadoDispositivoDto(
            cq.codigo,
            CASE
                WHEN ev.fechaResultado IS NOT NULL THEN 'Resultado Listo'
                WHEN ev.fechaExamen IS NOT NULL THEN 'En Proceso'
                WHEN dr.fechaRegistro IS NOT NULL THEN 'Registrado'
                ELSE 'Generado'
            END AS estado,
            dr.fechaRegistro,
            ev.fechaExamen,
            ev.fechaResultado,
            cq.fechaExpiracion
        )
        FROM CodigoQREntity cq
        LEFT JOIN DispositivoRegistradoEntity dr ON cq.codigo = dr.dispositivo
        LEFT JOIN ExamenVphEntity ev ON cq.codigo = ev.dispositivo
        WHERE dr.fechaRegistro >= :fechaInicio AND dr.fechaRegistro <= :fechaFin
        ORDER BY cq.codigo ASC
    """
        )
        fun findEstadoDispositivosConFiltro(
        @Param("fechaInicio") fechaInicio: LocalDateTime,
        @Param("fechaFin") fechaFin: LocalDateTime,
        pageable: Pageable
        ): Page<EstadoDispositivoDto>

    // Dispositivos registrados que NO tienen código QR generado por el admin
    @Query(
        """
        SELECT new com.crisordonez.registro.model.dtos.EstadoDispositivoDto(
            dr.dispositivo,
            CASE
                WHEN ev.fechaResultado IS NOT NULL THEN 'Resultado Listo'
                WHEN ev.fechaExamen    IS NOT NULL THEN 'En Proceso'
                ELSE 'Registrado'
            END,
            dr.fechaRegistro,
            ev.fechaExamen,
            ev.fechaResultado
        )
        FROM DispositivoRegistradoEntity dr
        LEFT JOIN ExamenVphEntity ev ON dr.dispositivo = ev.dispositivo
        WHERE NOT EXISTS (SELECT cq FROM CodigoQREntity cq WHERE cq.codigo = dr.dispositivo)
        ORDER BY dr.dispositivo ASC
    """
    )
    fun findDispositivosRegistradosSinQR(): List<EstadoDispositivoDto>

    @Query(
        """
        SELECT new com.crisordonez.registro.model.dtos.EstadoDispositivoDto(
            dr.dispositivo,
            CASE
                WHEN ev.fechaResultado IS NOT NULL THEN 'Resultado Listo'
                WHEN ev.fechaExamen    IS NOT NULL THEN 'En Proceso'
                ELSE 'Registrado'
            END,
            dr.fechaRegistro,
            ev.fechaExamen,
            ev.fechaResultado
        )
        FROM DispositivoRegistradoEntity dr
        LEFT JOIN ExamenVphEntity ev ON dr.dispositivo = ev.dispositivo
        WHERE NOT EXISTS (SELECT cq FROM CodigoQREntity cq WHERE cq.codigo = dr.dispositivo)
        AND dr.fechaRegistro >= :fechaInicio AND dr.fechaRegistro <= :fechaFin
        ORDER BY dr.dispositivo ASC
    """
    )
    fun findDispositivosRegistradosSinQRConFiltro(
        @Param("fechaInicio") fechaInicio: LocalDateTime,
        @Param("fechaFin") fechaFin: LocalDateTime
    ): List<EstadoDispositivoDto>

}