package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.RangoTiempoExamenEntity
import com.crisordonez.registro.model.requests.RangoTiempoExamenRequest
import com.crisordonez.registro.model.responses.RangoTiempoExamenResponse

object RangoTiempoExamenMapper {

    fun RangoTiempoExamenRequest.toEntity(): RangoTiempoExamenEntity = RangoTiempoExamenEntity(
        pregunta = this.pregunta.trim().uppercase(),
        codigo = this.codigo.trim().uppercase(),
        etiqueta = this.etiqueta.trim(),
        activo = this.activo,
        orden = this.orden
    )

    /** El código y la pregunta no se modifican en la actualización. */
    fun RangoTiempoExamenEntity.applyUpdate(req: RangoTiempoExamenRequest): RangoTiempoExamenEntity {
        this.etiqueta = req.etiqueta.trim()
        this.activo = req.activo
        this.orden = req.orden
        return this
    }

    fun RangoTiempoExamenEntity.toResponse(): RangoTiempoExamenResponse = RangoTiempoExamenResponse(
        publicId = this.publicId,
        pregunta = this.pregunta,
        codigo = this.codigo,
        etiqueta = this.etiqueta,
        activo = this.activo,
        orden = this.orden
    )
}
