package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.entities.SesionChatEntity
import com.crisordonez.registro.model.mapper.ExamenVphMapper.toResponse
import com.crisordonez.registro.model.requests.SesionChatRequest
import com.crisordonez.registro.model.responses.SesionChatResponse

object SesionChatMapper {

    /**
     * Fila creada al finalizar el automuestreo desde la app (botón "Automuestreo
     * completado"). Siempre `tipo = EXAMEN` y `origen = APP`.
     */
    fun SesionChatRequest.toEntity(
        paciente: PacienteEntity
    ): SesionChatEntity {
        return SesionChatEntity(
            tipo = "EXAMEN",
            origen = "APP",
            inicio = this.inicio,
            fin = this.fin,
            contenido = this.contenido,
            paciente = paciente,
        )
    }

    fun SesionChatRequest.toEntityUpdated(sesion: SesionChatEntity): SesionChatEntity {
        sesion.inicio = this.inicio
        sesion.fin = this.fin
        sesion.contenido = this.contenido
        return sesion
    }

    fun SesionChatEntity.toResponse(): SesionChatResponse {
        return SesionChatResponse(
            publicId = this.publicId,
            contenido = this.contenido,
            huboInteraccion = this.huboInteraccion,
            mensajesPaciente = this.mensajesPaciente,
            examenVph = this.examenVph?.toResponse()
        )
    }
}