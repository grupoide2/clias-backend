package com.crisordonez.registro.model.responses

data class CodigoQRValidacionResponse(
    val valido: Boolean,
    val expirado: Boolean,
    val mensaje: String
)
