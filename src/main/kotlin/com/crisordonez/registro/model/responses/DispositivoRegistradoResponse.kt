// src/main/kotlin/com/crisordonez/registro/model/responses/DispositivoRegistradoResponse.kt
package com.crisordonez.registro.model.responses

import java.time.LocalDateTime
import java.util.UUID

data class DispositivoRegistradoResponse(
    val dispositivo: String,
    val pacienteId: Long,
    val pacienteNombre: String,
    val publicId: UUID,
    val fechaRegistro: LocalDateTime,
    val cuentaUsuarioPublicId: UUID?
)
