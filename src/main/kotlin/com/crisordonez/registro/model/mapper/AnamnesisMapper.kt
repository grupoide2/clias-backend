package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.AnamnesisEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.requests.AnamnesisRequest
import com.crisordonez.registro.model.responses.AnamnesisResponse

object AnamnesisMapper {

    fun AnamnesisRequest.toEntity(paciente: PacienteEntity): AnamnesisEntity {
        return AnamnesisEntity(
            edadPrimerRelacionSexual = this.edadPrimerRelacionSexual,
            edadPrimerPap = this.edadPrimerPap,
            paciente = paciente
        )
    }

    fun AnamnesisEntity.toResponse(): AnamnesisResponse {
        return AnamnesisResponse(
            publicId = this.publicId,
            sexual = this.edadPrimerRelacionSexual,
            edadPrimerPap = this.edadPrimerPap,
            campo = "hola"
        )
    }

    fun AnamnesisRequest.toEntityUpdated(anamnesis: AnamnesisEntity): AnamnesisEntity {
        anamnesis.edadPrimerPap = this.edadPrimerPap
        anamnesis.edadPrimerRelacionSexual = this.edadPrimerRelacionSexual
        return anamnesis
    }

}