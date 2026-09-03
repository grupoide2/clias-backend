package com.crisordonez.registro.model.requests

import java.time.LocalDateTime

data class SesionChatbotFinRequest(
    val fin: LocalDateTime,
    /** Nº de mensajes que la paciente envió al chatbot durante la sesión. */
    val mensajesPaciente: Int = 0
)
