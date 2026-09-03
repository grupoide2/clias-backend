package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.ExamenVphEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface ExamenVphRepository : CrudRepository<ExamenVphEntity, Long> {

    fun findByDispositivo(dispositivo: String): Optional<ExamenVphEntity>

    fun findFirstByDispositivo(dispositivo: String): Optional<ExamenVphEntity>

    fun findByPublicId(publicId: UUID): ExamenVphEntity?
    @Query("SELECT e FROM ExamenVphEntity e WHERE e.saludSexual.id = :saludSexualId")

    fun findBySaludSexualId(@Param("saludSexualId") saludSexualId: Long): Optional<ExamenVphEntity>
    @Modifying
    @Transactional
    @Query(
        """
        UPDATE ExamenVphEntity e
        SET 
          e.contenido = NULL,
          e.fechaResultado = NULL,
          e.nombre = NULL,
          e.tamano = NULL,
          e.tipo = NULL,
          e.diagnostico = NULL
        WHERE e.dispositivo = :dispositivo
        """
    )
    fun clearFieldsByCodigo(@Param("dispositivo") dispositivo: String): Int

    @Query("""
    SELECT COUNT(e) > 0
    FROM ExamenVphEntity e
    WHERE e.sesionChat.paciente.id = :pacienteId
""")
    fun existsExamenByPacienteId(@Param("pacienteId") pacienteId: Long): Boolean

    @Query("""
    SELECT e.dispositivo
    FROM ExamenVphEntity e
    WHERE e.sesionChat.paciente.id = :pacienteId
    ORDER BY e.id DESC
""")
    fun findDispositivosByPacienteIdOrderByIdDesc(@Param("pacienteId") pacienteId: Long): List<String>

    @Query("SELECT COUNT(e) FROM ExamenVphEntity e WHERE e.fechaResultado IS NOT NULL")
    fun countConResultado(): Long

    @Query("""
    SELECT COUNT(ev) > 0
    FROM ExamenVphEntity ev
    WHERE ev.contenido IS NOT NULL
    AND ev.sesionChat.paciente.cuenta.publicId = :cuentaUsuarioPublicId
""")
    fun existsExamenEntregadoByCuentaUsuarioPublicId(
        @Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID
    ): Boolean



}
