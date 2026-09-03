package com.crisordonez.registro.model.responses

import java.util.UUID

data class SesionChatResponse(

    val publicId: UUID,

    val contenido: String?,

    val huboInteraccion: Boolean = false,

    val mensajesPaciente: Int = 0,

    val examenVph: ExamenVphResponse? = null

)
