package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class DispositivoAppUsuarioRequest(
    @field:NotNull(message = "El UUID del usuario es requerido")
    val usuarioPublicId: UUID,

    @field:NotBlank(message = "El token FCM es requerido")
    val fcmToken: String,

)