package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.OcupacionEntity
import com.crisordonez.registro.model.requests.OcupacionRequest
import com.crisordonez.registro.model.responses.OcupacionResponse

object OcupacionMapper {

    fun OcupacionRequest.toEntity(): OcupacionEntity = OcupacionEntity(
        nombre = this.nombre.trim(),
        activo = this.activo,
        soloUniversidad = this.soloUniversidad,
        orden = this.orden
    )

    fun OcupacionEntity.applyUpdate(req: OcupacionRequest): OcupacionEntity {
        this.nombre = req.nombre.trim()
        this.activo = req.activo
        this.soloUniversidad = req.soloUniversidad
        this.orden = req.orden
        return this
    }

    fun OcupacionEntity.toResponse(): OcupacionResponse = OcupacionResponse(
        publicId = this.publicId,
        nombre = this.nombre,
        activo = this.activo,
        soloUniversidad = this.soloUniversidad,
        orden = this.orden
    )
}
