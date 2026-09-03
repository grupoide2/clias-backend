package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class SesionChatRequest(

    @field:NotNull(message = "La cuenta del usuario es requerida")
    val cuentaPublicId: UUID,

    @field: NotNull(message = "El tiempo de inicio es requerido")
    val inicio: LocalDateTime,

    @field: NotNull(message = "El tiempo de fin es requerido")
    val fin: LocalDateTime,

    val contenido: String? = null,

    val examenVph: ExamenVphRequest? = null

)
