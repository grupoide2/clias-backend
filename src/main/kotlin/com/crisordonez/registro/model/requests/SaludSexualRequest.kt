package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.OpcionesEnum
import com.crisordonez.registro.model.enums.OpcionesSiNoEnum
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SaludSexualRequest(

    val estaEmbarazada: Boolean = false,

    @field:NotNull(message = "Fecha de última menstruación es requerida")
    val fechaUltimaMenstruacion: String,

    // Código del catálogo rango_tiempo_examen (ej. DE_3_A_6_MESES).
    @field:NotBlank(message = "Último examen de PAP es requerido")
    val ultimoExamenPap: String,

    @field:NotBlank(message = "Tiempo de prueba de VPH es requerido")
    val tiempoPruebaVph: String,

    @field:NotNull(message = "Número de parejas sexuales es requerido")
    val numParejasSexuales: Int,

    @field:NotNull(message = "Tiene ETS es requerido")
    val tieneEts: OpcionesEnum,

    val nombreEts: String? = null,

    val estaMenstruando: OpcionesSiNoEnum? = null

)
