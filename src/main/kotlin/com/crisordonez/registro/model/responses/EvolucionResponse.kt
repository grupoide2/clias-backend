package com.crisordonez.registro.model.responses

import java.time.LocalDateTime
import java.util.UUID


data class EvolucionResponse(

    val publicId: UUID,

    val temperatura: Double,

    val pulso: Int,

    val talla: Double,

    val peso: Double,
  
    val fecha: LocalDateTime

)






