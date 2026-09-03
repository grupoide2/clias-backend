package com.crisordonez.registro.model.responses

import java.util.UUID

data class AnamnesisResponse(

    val publicId: UUID,

    val sexual: Int? = null,

    val edadPrimerPap: Int? = null,

    val campo: String

)
