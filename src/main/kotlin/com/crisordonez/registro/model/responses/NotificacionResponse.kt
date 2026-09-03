package com.crisordonez.registro.model.responses

import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import java.time.LocalDateTime
import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty

data class NotificacionResponse(
    val publicId: UUID,
    val cuentaUsuarioPublicId: UUID,
    val tipoNotificacion: TipoNotificacionEnum,
    val titulo: String,
    val mensaje: String,
    val tipoAccion: TipoAccionNotificacionEnum,
    val accion: String?,
    val fechaCreacion: LocalDateTime,
    @JsonProperty("leido")
    val notificacionLeida: Boolean
)
