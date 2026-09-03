package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.FolletoRequest
import com.crisordonez.registro.model.requests.SesionChatRequest
import com.crisordonez.registro.model.requests.SesionChatbotInicioRequest
import com.crisordonez.registro.model.requests.SesionChatbotFinRequest
import com.crisordonez.registro.model.responses.SesionChatResponse
import java.util.UUID

interface SesionChatServiceInterface {

    fun crearSesionChat(sesion: SesionChatRequest)

    fun crearSesionChatFolleto(request: FolletoRequest)

    fun getSesionChat(publicId: UUID): List<SesionChatResponse>

    fun getTodosSesionChat(): List<SesionChatResponse>

    fun deleteSesionChat(publicId: UUID)

    fun iniciarSesionChatbot(request: SesionChatbotInicioRequest): UUID

    fun finalizarSesionChatbot(sessionPublicId: UUID, request: SesionChatbotFinRequest)

}