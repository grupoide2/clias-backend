package com.crisordonez.registro.model.responses

data class MetricaDashboardResponse(
    val pacientesRegistradas: Long,
    val automuestreosRealizados: Long,
    val pacientesUsaronChatbot: Long,
    val sesionesChatbot: Long,
    val mensajesEnviadosChatbot: Long,
    val tiempoPromedioAppSeg: Long,
    val tiempoPromedioAppFormato: String,
    val tiempoPromedioChatbotSeg: Long,
    val tiempoPromedioChatbotFormato: String,
    val resultadosDisponibles: Long
)
