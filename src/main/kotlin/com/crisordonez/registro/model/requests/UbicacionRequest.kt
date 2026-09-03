package com.crisordonez.registro.model.requests
import jakarta.validation.constraints.*
data class UbicacionRequest(
    @field:NotBlank val nombre: String,
    @field:NotBlank val direccion: String,
    val telefono: String,
    val horario: String,
    val sitioWeb: String,
    val latitud: Double,
    val longitud: Double,
    val establecimiento: String // lo que llega en el JSON
)