package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.entities.NotificacionProgramadaEntity
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import java.time.LocalDateTime

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface NotificacionProgramadaRepository : JpaRepository<NotificacionProgramadaEntity, Long> {

    @Query("""
    SELECT np 
    FROM NotificacionProgramadaEntity np 
    WHERE np.proxFecha <= :now 
      AND np.programacionActiva = true
""")
    fun findAllByProxFechaBeforeAndProgramacionActivaIsTrue(
        @Param("now") now: LocalDateTime
    ): List<NotificacionProgramadaEntity>


    fun findByPublicId(publicId: UUID): Optional<NotificacionProgramadaEntity>

    @Query("""
    SELECT np
    FROM NotificacionProgramadaEntity np
    WHERE np.cuentaUsuario.publicId = :cuentaUsuarioPublicId
      AND np.tipoNotificacion = :tipoNotificacion
      AND np.programacionActiva = true
""")
    fun findActivaByCuentaUsuarioPublicIdAndTipoNotificacion(
        @Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID,
        @Param("tipoNotificacion") tipoNotificacion: TipoNotificacionEnum
    ): Optional<NotificacionProgramadaEntity>

    @Modifying
    @Transactional
    @Query("""
    UPDATE NotificacionProgramadaEntity np
    SET np.programacionActiva = :estado
    WHERE np.id = :id
""")
    fun updateActivaById(
        @Param("estado") estado: Boolean,
        @Param("id") id: Long
    )

    @Modifying
    @Transactional
    @Query("""
    UPDATE NotificacionProgramadaEntity np
    SET np.programacionActiva = false
    WHERE np.cuentaUsuario.publicId = :cuentaUsuarioPublicId
      AND np.tipoNotificacion = :tipoNotificacion
      AND np.programacionActiva = true
""")
    fun desactivarActivasByCuentaUsuarioPublicIdAndTipo(
        @Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID,
        @Param("tipoNotificacion") tipoNotificacion: TipoNotificacionEnum
    ): Int

    fun existsByCuentaUsuarioAndTipoNotificacionAndProgramacionActivaTrue(
        cuentaUsuario: CuentaUsuarioEntity,
        tipoNotificacion: TipoNotificacionEnum
    ): Boolean


}
