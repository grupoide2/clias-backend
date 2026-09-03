package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotBlank

data class OcupacionRequest(
    @field:NotBlank val nombre: String,
    val activo: Boolean = true,
    val soloUniversidad: Boolean = false,
    val orden: Int = 0
)
