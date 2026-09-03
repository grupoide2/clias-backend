package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.ArchivoEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface ArchivoRepository: CrudRepository<ArchivoEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<ArchivoEntity>

    fun findByNombre(nombre: String): Optional<ArchivoEntity>

}