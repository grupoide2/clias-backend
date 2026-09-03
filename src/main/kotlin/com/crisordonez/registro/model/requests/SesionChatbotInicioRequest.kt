package com.crisordonez.registro.model.requests

import java.time.LocalDateTime
import java.util.UUID

data class SesionChatbotInicioRequest(
    val cuentaPublicId: UUID,
    val inicio: LocalDateTime
)
