package com.crisordonez.registro.model.responses

import com.crisordonez.registro.model.enums.SexoEnum
import java.util.UUID

data class MedicoResponse(

    val publicId: UUID,

    val usuario: String,

    val nombre: String,

    val correo: String,

    val especializacion: String? = null,

    val sexo: SexoEnum,

    val nRegistro: String,

    val evoluciones: List<EvolucionResponse> = emptyList()

)


