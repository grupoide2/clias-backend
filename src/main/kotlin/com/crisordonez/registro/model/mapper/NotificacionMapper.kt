package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.NotificacionEntity
import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.model.responses.NotificacionResponse

object NotificacionMapper {

    fun NotificacionRequest.toEntity(cuentaUsuario: CuentaUsuarioEntity): NotificacionEntity {
        return NotificacionEntity(
            cuentaUsuario = cuentaUsuario,
            tipo_notificacion = this.tipoNotificacion,
            titulo = this.titulo,
            mensaje = this.mensaje,
            tipo_accion = this.tipoAccion,
            accion = this.accion
        )
    }

    fun NotificacionEntity.toResponse(): NotificacionResponse {
        return NotificacionResponse(
            publicId = this.publicId,
            tipoNotificacion = this.tipo_notificacion,
            titulo = this.titulo,
            mensaje = this.mensaje,
            tipoAccion = this.tipo_accion,
            accion = this.accion,
            fechaCreacion = this.fecha_creacion,
            notificacionLeida = this.notificacion_leida,
            cuentaUsuarioPublicId = this.cuentaUsuario.publicId
        )
    }
}
