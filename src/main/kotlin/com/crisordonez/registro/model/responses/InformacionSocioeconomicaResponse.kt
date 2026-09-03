package com.crisordonez.registro.model.responses

import java.util.UUID

data class InformacionSocioeconomicaResponse(

    val publicId: UUID,

    val instruccion: String? = null,

    val ingresos: String? = null,

    val trabajoRemunerado: String? = null,

    val ocupacion: String? = null,

    val recibeBono: String? = null,

    val dependenciaUniversitaria: String? = null

)
