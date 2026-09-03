package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.OcupacionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface OcupacionRepository : JpaRepository<OcupacionEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<OcupacionEntity>

    fun findByNombreIgnoreCase(nombre: String): Optional<OcupacionEntity>

    fun existsByNombreIgnoreCase(nombre: String): Boolean

    fun findAllByActivoTrueOrderByOrdenAscNombreAsc(): List<OcupacionEntity>

    fun findAllByOrderByOrdenAscNombreAsc(): List<OcupacionEntity>
}
