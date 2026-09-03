package com.crisordonez.registro.model.responses

import java.time.LocalDate

data class CodigoQRStatusResponse(
    val codigo: String,
    val fechaExpiracion: LocalDate,
    val status: String
)
