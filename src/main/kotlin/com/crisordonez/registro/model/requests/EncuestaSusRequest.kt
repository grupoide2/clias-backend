package com.crisordonez.registro.model.requests

data class EncuestaSusRequest(
    val respuestas: List<Int>,
    val cuentaUsuarioId: String
)

