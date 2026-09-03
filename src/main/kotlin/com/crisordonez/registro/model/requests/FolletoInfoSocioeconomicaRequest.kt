package com.crisordonez.registro.model.requests

/**
 * Subconjunto de información socioeconómica que recoge el folleto de clias-admin.
 * Todo opcional: si viene null no se crea la ficha socioeconómica.
 */
data class FolletoInfoSocioeconomicaRequest(
    val ingresos: String? = null,                 // nombre de IngresoEnum
    val dependenciaUniversitaria: String = "NO",   // SI / NO
    val ocupacion: String? = null                  // texto libre o nombre del catálogo
)
