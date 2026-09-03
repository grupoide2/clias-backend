package com.crisordonez.registro.model.dtos

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class EstadoDispositivoDto(
    val codigo: String,
    val estado: String,
    val fechaRegistro: LocalDateTime?,
    val fechaExamen: LocalDateTime?,
    val fechaResultado: Date?,
    val fechaExpiracion: LocalDate?
) {
    // Constructor para dispositivos sin código QR (fechaExpiracion = null)
    constructor(
        codigo: String,
        estado: String,
        fechaRegistro: LocalDateTime?,
        fechaExamen: LocalDateTime?,
        fechaResultado: Date?
    ) : this(codigo, estado, fechaRegistro, fechaExamen, fechaResultado, null)
}
