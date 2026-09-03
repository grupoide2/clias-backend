package com.crisordonez.registro.model.responses

import java.util.UUID

data class CuentaUsuarioResponse(

    val publicId: UUID,

    val nombre: String,

    val nombreUsuario: String,

    val token: String? = null,

    val dispositivo: String? = null

)
