package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.responses.NotificacionResponse
import java.util.UUID

interface PushNotificacionServiceInterface {
    fun enviarPushFCM(usuarioPublicId: UUID, notificacion: NotificacionResponse) {
    }
}
