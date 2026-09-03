package com.crisordonez.registro.model.responses

import java.util.UUID

data class RangoTiempoExamenResponse(
    val publicId: UUID,
    val pregunta: String,
    val codigo: String,
    val etiqueta: String,
    val activo: Boolean,
    val orden: Int
)
