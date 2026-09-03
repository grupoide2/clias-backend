// src/main/kotlin/com/crisordonez/registro/repository/AdministradorRepository.kt
package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.Administrador
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdministradorRepository : JpaRepository<Administrador, Long> {

    fun findByPublicId(publicId: UUID): Administrador?
    fun existsByPublicId(publicId: UUID): Boolean
    fun deleteByPublicId(publicId: UUID)
    fun findByUsuario(usuario: String): Administrador?
}
