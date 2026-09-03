package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotBlank

data class RangoTiempoExamenRequest(
    /** Pregunta a la que pertenece: MENSTRUACION | PAPANICOLAOU | VPH. Solo se usa al crear. */
    @field:NotBlank val pregunta: String = "PAPANICOLAOU",
    /** Solo se usa al crear; en la actualización el código es inmutable. */
    @field:NotBlank val codigo: String,
    @field:NotBlank val etiqueta: String,
    val activo: Boolean = true,
    val orden: Int = 0
)
