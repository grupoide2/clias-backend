package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.MedicoRequest
import com.crisordonez.registro.model.responses.MedicoResponse
import java.util.UUID

interface MedicoServiceInterface {

    fun createMedico(medico: MedicoRequest)

    fun getMedico(publicId: UUID): MedicoResponse

    fun getAllMedicos(): List<MedicoResponse>

    fun updateMedico(publicId: UUID, medico: MedicoRequest)

    fun deleteMedico(publicId: UUID)

}