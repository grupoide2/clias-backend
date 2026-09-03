package com.crisordonez.registro.model.responses

import java.util.UUID

data class OcupacionResponse(
    val publicId: UUID,
    val nombre: String,
    val activo: Boolean,
    val soloUniversidad: Boolean,
    val orden: Int
)
