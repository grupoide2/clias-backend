package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.UbicacionEntity
import com.crisordonez.registro.model.enums.EstablecimientoEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UbicacionRepository: JpaRepository<UbicacionEntity, Long> {

    fun findByPublicId(publicId: UUID): UbicacionEntity?

    fun findByEstablecimiento(establecimiento: EstablecimientoEnum): List<UbicacionEntity>

    fun deleteByPublicId(publicId: UUID)
}