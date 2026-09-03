package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ExamenResultadoRequest(

    @field:NotNull(message = "La fecha del resultado es obligatoria")
    @field:NotBlank(message = "La fecha del resultado no debe estar vacía")
    val fechaResultado: String,

    val archivo: ArchivoRequest? = null,

    val evolucion: EvolucionRequest? = null,

    @field:NotBlank(message = "El diagnóstico no debe estar vacío")
    val diagnostico: String

)
