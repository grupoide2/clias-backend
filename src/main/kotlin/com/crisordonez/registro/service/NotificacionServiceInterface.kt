package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.model.requests.NotificacionProgramadaRequest
import com.crisordonez.registro.model.responses.NotificacionResponse
import java.util.UUID

interface NotificacionServiceInterface {
    fun crearNotificacion(request: NotificacionRequest): NotificacionResponse
    fun obtenerHistorialNotificaciones(cuentaUsuarioPublicId: UUID): List<NotificacionResponse>
    fun marcarNotificacionComoLeida(publicId: UUID)

    fun crearNotificacionProgramada(
        requestNotificacion: NotificacionRequest,
        requestProgramada: NotificacionProgramadaRequest
    ): NotificacionResponse

    fun procesarNotificacionesProgramadas()
    fun desactivarRecordatorioNoEntregaDispositivo(cuentaUsuarioPublicId: UUID)

    /** Desactiva los recordatorios "realízate el examen" cuando la paciente ya completó el automuestreo. */
    fun desactivarRecordatorioNoExamen(cuentaUsuarioPublicId: UUID)

}
