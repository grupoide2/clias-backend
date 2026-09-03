package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO para crear una programación recurrente de notificación
 */
data class NotificacionProgramadaRequest(
    @field:NotNull(message = "El ID de la notificación base es requerido")
    val notificacionPublicId: UUID,
    val tipoNotificacion: TipoNotificacionEnum,
    val titulo: String,
    val mensaje: String,
    val tipoAccion: TipoAccionNotificacionEnum,
    val accion: String?,
    @field:Future(message = "La fecha de próxima ejecución debe ser futura")
    val proxFecha: LocalDateTime,
    val limiteFecha: LocalDateTime? = null

)
