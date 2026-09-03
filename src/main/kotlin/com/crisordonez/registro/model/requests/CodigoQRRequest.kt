package com.crisordonez.registro.model.requests

import java.time.LocalDate

data class CodigoQRRequest(
    val codigo: String,
    val status: String,
    val imagenBase64: String,
    val fechaExpiracion: LocalDate
)
