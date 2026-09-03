// src/main/kotlin/com/crisordonez/registro/service/PacienteServiceInterface.kt
package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.requests.DispositivoRegistradoRequest
import com.crisordonez.registro.model.requests.PacienteRequest
import com.crisordonez.registro.model.responses.EstadoAutomuestreoResponse
import com.crisordonez.registro.model.responses.PacienteResponse
import java.util.UUID

interface PacienteServiceInterface {
    fun editarPaciente(publicId: UUID, paciente: PacienteRequest)
    fun getPaciente(publicId: UUID): PacienteResponse
    fun getTodosPacientes(): List<PacienteResponse>
    // Busca un paciente a partir del código (dispositivo) registrado.
    fun findByDispositivo(codigo: String): PacienteResponse?
    fun registrarDispositivo(publicId: UUID, dispositivo: DispositivoRegistradoRequest): String
    fun obtenerPublicIdPorIdInterno(id: Long): UUID
    fun estadoAutomuestreo(publicId: UUID): EstadoAutomuestreoResponse

}

