package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.EncuestaSusRequest

interface EncuestaSusServiceInterface {
    fun guardarEncuesta(request: EncuestaSusRequest): Boolean
    fun estaEncuestaCompletada(cuentaUsuarioId: String): Boolean
}
