package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.RecursoMultimediaEntity
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface RecursoMultimediaRepository : JpaRepository<RecursoMultimediaEntity, Long> {

    fun findBySlug(slug: String): Optional<RecursoMultimediaEntity>

    fun findByPublicId(publicId: UUID): Optional<RecursoMultimediaEntity>

    fun findAllByOrderByOrdenAscSlugAsc(): List<RecursoMultimediaEntity>

    fun findAllByActivoTrueOrderByOrdenAscSlugAsc(): List<RecursoMultimediaEntity>

    fun findAllByTipoAndActivoTrueOrderByOrdenAscSlugAsc(tipo: TipoArchivoEnum): List<RecursoMultimediaEntity>

    fun findAllByTipoOrderByOrdenAscSlugAsc(tipo: TipoArchivoEnum): List<RecursoMultimediaEntity>
}
