package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.SaludSexualEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface SaludSexualRepository: CrudRepository< SaludSexualEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<SaludSexualEntity>

}