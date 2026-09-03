package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class EvolucionRequest(

    val temperatura: Double = 0.0,

    val pulso: Int = 0,

    val talla: Double = 0.0,

    val peso: Double = 0.0,

    @field:NotNull(message = "La fecha no puede ser nula")
    val fecha: LocalDateTime

)
