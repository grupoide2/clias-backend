package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.EvolucionEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface EvolucionRepository: CrudRepository<EvolucionEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<EvolucionEntity>

}