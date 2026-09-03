package com.crisordonez.registro.model.responses

import java.util.UUID

data class UbicacionResponse(
    val publicId: UUID,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val horario: String,
    val sitioWeb: String,
    val latitud: Double,
    val longitud: Double,
    val establecimiento: String
)

data class UbicacionRechazadaResponse(
    val nombre: String,
    val direccion: String,
    val motivo: String
)