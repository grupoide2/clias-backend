package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.AnamnesisEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface AnamnesisRepository: CrudRepository<AnamnesisEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<AnamnesisEntity>

}