package com.crisordonez.registro.service

import com.crisordonez.registro.model.responses.MetricaDashboardResponse
import java.time.LocalDateTime
import java.util.UUID

interface MetricaUsoServiceInterface {

    /** Indicadores agregados para el dashboard de clias-admin. */
    fun dashboard(): MetricaDashboardResponse

    /** +1 a sesiones_chatbot_totales. Se llama al ingresar al chatbot. */
    fun registrarInicioSesionChatbot(cuentaPublicId: UUID)

    /**
     * Cierre de una sesión de chatbot: acumula mensajes y tiempo de chat;
     * +1 a sesiones_chatbot_exitosas si hubo al menos un mensaje de la paciente.
     */
    fun registrarFinSesionChatbot(cuentaPublicId: UUID, mensajesPaciente: Int, duracionSeg: Long)

    /** +1 a sesiones_chatbot_exitosas al completar el formulario de automuestreo. */
    fun registrarAutomuestreoCompletado(cuentaPublicId: UUID)

    /** Camino legado de `PUT /usuarios/chat-time/{publicId}` (la app aún lo usa hasta Fase 2). */
    fun acumularTiempoChat(cuentaPublicId: UUID, tiempoMinutos: Double)

    /** Acumula tiempo de uso de la app; fija primer_uso_app una sola vez y actualiza ultimo_uso_app. */
    fun acumularTiempoApp(cuentaPublicId: UUID, inicio: LocalDateTime, fin: LocalDateTime, tiempoMinutos: Double)

    /** Promedio de tiempo de uso de chat, en minutos, sobre pacientes con tiempo registrado. */
    fun promedioTiempoChatMinutos(): Double
}
