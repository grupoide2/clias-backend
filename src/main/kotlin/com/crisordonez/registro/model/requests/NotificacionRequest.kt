package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import jakarta.validation.constraints.NotNull
import java.util.UUID

/**
 * DTO para crear una notificación puntual o plantilla programada.
 */
data class NotificacionRequest(

    @field:NotNull(message = "El ID de la cuenta de usuario es requerido")
    val cuentaUsuarioPublicId: UUID,

    @field:NotNull(message = "El tipo de notificación es requerido")
    val tipoNotificacion: TipoNotificacionEnum,

    @field:NotNull(message = "El título es requerido")
    val titulo: String,

    @field:NotNull(message = "El mensaje es requerido")
    val mensaje: String,

    @field:NotNull(message = "El tipo de acción es requerido")
    val tipoAccion: TipoAccionNotificacionEnum,

    val accion: String? = null
)
