package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.AnamnesisRequest
import com.crisordonez.registro.model.responses.AnamnesisResponse
import java.util.UUID

interface AnamnesisServiceInterface {

    fun crearAnamnesis(publicId: UUID, anamnesis: AnamnesisRequest)

    fun getAnamnesis(publicId: UUID): AnamnesisResponse

    fun getTodasAnamnesis(): List<AnamnesisResponse>

    fun editarAnamnesis(publicId: UUID, anamnesis: AnamnesisRequest)

    fun eliminarAnamnesis(publicId: UUID)

}