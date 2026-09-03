package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.RangoTiempoExamenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface RangoTiempoExamenRepository : JpaRepository<RangoTiempoExamenEntity, Long> {

    fun findByPublicId(publicId: UUID): Optional<RangoTiempoExamenEntity>

    fun findByCodigo(codigo: String): Optional<RangoTiempoExamenEntity>

    fun existsByCodigo(codigo: String): Boolean

    fun findAllByActivoTrueOrderByOrdenAscEtiquetaAsc(): List<RangoTiempoExamenEntity>

    fun findAllByOrderByOrdenAscEtiquetaAsc(): List<RangoTiempoExamenEntity>

    fun findAllByPreguntaAndActivoTrueOrderByOrdenAscEtiquetaAsc(pregunta: String): List<RangoTiempoExamenEntity>

    fun findAllByPreguntaOrderByOrdenAscEtiquetaAsc(pregunta: String): List<RangoTiempoExamenEntity>
}
