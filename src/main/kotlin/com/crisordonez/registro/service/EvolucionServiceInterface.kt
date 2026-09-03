package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.EvolucionRequest
import com.crisordonez.registro.model.responses.EvolucionResponse

import java.util.UUID

interface EvolucionServiceInterface {

    fun crearEvolucion(publicId: String, evolucion: EvolucionRequest)

    fun getEvolucion(publicId: UUID): EvolucionResponse

    fun getTodasEvoluciones(): List<EvolucionResponse>

    fun eliminarEvolucion(publicId: UUID)


}