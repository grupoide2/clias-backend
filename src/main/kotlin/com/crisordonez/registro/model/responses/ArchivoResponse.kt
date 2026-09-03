package com.crisordonez.registro.model.responses

import java.util.UUID

data class ArchivoResponse(

    val publicId: UUID,

    val tipo: String,

    val tamano: Long,

    val nombre: String,

    val contenido: ByteArray

)
