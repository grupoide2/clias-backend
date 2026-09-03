// src/main/kotlin/com/crisordonez/registro/repository/PacienteRepository.kt
package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.PacienteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface PacienteRepository : JpaRepository<PacienteEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<PacienteEntity>
    fun existsByPublicId(publicId: UUID): Boolean
    fun deleteByPublicId(publicId: UUID)
    fun findByCuentaPublicId(cuentaPublicId: UUID): Optional<PacienteEntity>
    @Query("SELECT p.publicId FROM PacienteEntity p WHERE p.id = :id")
    fun findPublicIdById(@Param("id") id: Long): UUID?

}
