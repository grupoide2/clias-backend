package com.crisordonez.registro.model.responses

import java.time.LocalDateTime
import java.util.*

data class DispositivoAppUsuarioResponse(
    val publicId: UUID,
    val usuarioPublicId: UUID,
    val fcmToken: String,
    val fechaRegistro: LocalDateTime
)