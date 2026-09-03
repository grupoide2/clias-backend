package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.requests.UbicacionRequest
import com.crisordonez.registro.model.entities.UbicacionEntity
import com.crisordonez.registro.model.enums.EstablecimientoEnum
import com.crisordonez.registro.model.responses.UbicacionResponse

object UbicacionMapper {

    fun toEntity(dto: UbicacionRequest): UbicacionEntity {
        return UbicacionEntity(
            nombre = dto.nombre,
            telefono = dto.telefono,
            direccion = dto.direccion,
            horario = dto.horario,
            sitioWeb = dto.sitioWeb,
            latitud = dto.latitud,
            longitud = dto.longitud,
            establecimiento = EstablecimientoEnum.valueOf(dto.establecimiento)
        )
    }

    fun toResponse(entity: UbicacionEntity): UbicacionResponse {
        return UbicacionResponse(
            publicId = entity.publicId,
            nombre = entity.nombre,
            telefono = entity.telefono,
            direccion = entity.direccion,
            horario = entity.horario,
            sitioWeb = entity.sitioWeb,
            latitud = entity.latitud,
            longitud = entity.longitud,
            establecimiento = entity.establecimiento.toString()
        )
    }

    fun toResponseList(entities: List<UbicacionEntity>): List<UbicacionResponse> =
        entities.map { toResponse(it) }
}
