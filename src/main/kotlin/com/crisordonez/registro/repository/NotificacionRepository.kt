package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.NotificacionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.util.*

interface NotificacionRepository : JpaRepository<NotificacionEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<NotificacionEntity>

    @Query("""
    SELECT n 
    FROM NotificacionEntity n 
    WHERE n.cuentaUsuario.publicId = :cuentaUsuarioPublicId 
    ORDER BY n.fecha_creacion DESC
    """)
    fun findAllByCuentaUsuarioPublicIdOrderByFechaCreacionDesc(
        @Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID
    ): List<NotificacionEntity>


    // Listar todas las notificaciones de un paciente no leídas
    @Query("""
    SELECT n 
    FROM NotificacionEntity n 
    WHERE n.cuentaUsuario.publicId = :cuentaUsuarioPublicId 
    AND n.notificacion_leida = false 
    ORDER BY n.fecha_creacion DESC
    """)
    fun findAllByCuentaUsuarioPublicIdAndNotificacionNoLeidaOrderByFechaCreacionDesc(
        @Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID
    ): List<NotificacionEntity>

}
