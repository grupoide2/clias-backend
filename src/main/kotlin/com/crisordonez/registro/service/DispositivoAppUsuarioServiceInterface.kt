package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.DispositivoAppUsuarioRequest
import com.crisordonez.registro.model.responses.DispositivoAppUsuarioResponse
import java.util.*

interface DispositivoAppUsuarioServiceInterface {
    fun registrarDispositivo(request: DispositivoAppUsuarioRequest)
    fun listarDispositivosPorUsuario(usuarioPublicId: UUID): List<DispositivoAppUsuarioResponse>
    fun eliminarTokenFCMInvalido(fcmToken: String)
}