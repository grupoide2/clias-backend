package com.crisordonez.registro.model.requests

import java.time.LocalDateTime

data class TiempoAppRequest(
    val inicio: LocalDateTime,
    val fin: LocalDateTime,
    val tiempo: Double // minutos
)
