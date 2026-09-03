package com.crisordonez.registro.model.responses

import java.util.UUID

data class SaludSexualResponse(

    val publicId: UUID,

    val estaEmbarazada: Boolean,

    val fechaUltimaMenstruacion: String,

    val ultimoExamenPap: String,

    val tiempoPruebaVph: String,

    val numParejasSexuales: Int,

    val tieneEts: String,

    val nombreEts: String? = null,

    val estaMenstruando: String? = null

)
