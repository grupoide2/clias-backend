package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.entities.NotificacionProgramadaEntity
import com.crisordonez.registro.model.requests.NotificacionProgramadaRequest
import com.crisordonez.registro.model.responses.NotificacionProgramadaResponse

object NotificacionProgramadaMapper {

    fun NotificacionProgramadaRequest.toEntity(cuenta: CuentaUsuarioEntity): NotificacionProgramadaEntity {
        return NotificacionProgramadaEntity(
            cuentaUsuario = cuenta,
            tipoNotificacion = this.tipoNotificacion,
            titulo = this.titulo,
            mensaje = this.mensaje,
            tipoAccion = this.tipoAccion,
            accion = this.accion,
            proxFecha = this.proxFecha,
            limiteFecha = this.limiteFecha
        )
    }

    fun NotificacionProgramadaEntity.toResponse(): NotificacionProgramadaResponse {
        return NotificacionProgramadaResponse(
            publicId = this.publicId,
            cuentaUsuarioPublicId = this.cuentaUsuario.publicId,
            tipoNotificacion = this.tipoNotificacion,
            titulo = this.titulo,
            mensaje = this.mensaje,
            tipoAccion = this.tipoAccion,
            accion = this.accion,
            proxFecha = this.proxFecha,
            activa = this.programacionActiva,
            limiteFecha = this.limiteFecha
        )
    }
}

