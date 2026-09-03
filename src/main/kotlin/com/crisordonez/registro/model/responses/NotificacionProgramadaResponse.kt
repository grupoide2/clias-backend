package com.crisordonez.registro.model.responses

import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO de respuesta para la programación recurrente
 */
data class NotificacionProgramadaResponse(
    val publicId: UUID,
    val cuentaUsuarioPublicId: UUID,
    val tipoNotificacion: TipoNotificacionEnum,
    val titulo: String,
    val mensaje: String,
    val tipoAccion: TipoAccionNotificacionEnum,
    val accion: String?,
    val proxFecha: LocalDateTime,
    val activa: Boolean,
    val limiteFecha: LocalDateTime?
)
