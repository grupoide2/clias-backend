package com.crisordonez.registro.model.responses

/**
 * Estado del automuestreo de una paciente, para que la app móvil sepa si debe
 * bloquear el botón "Iniciar proceso de Automuestreo" y con qué código mostrarlo.
 */
data class EstadoAutomuestreoResponse(
    val completado: Boolean,
    val codigoDispositivo: String?
)
