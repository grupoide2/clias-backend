// src/main/kotlin/com/crisordonez/registro/model/responses/AuthResponse.kt
package com.crisordonez.registro.model.responses

import java.util.UUID

data class AuthResponse(
    val publicId: UUID,
    val nombre:   String,
    val usuario:  String,
    val role:     String,   // "ADMIN" o "DOCTOR"
    val token:    String
)
