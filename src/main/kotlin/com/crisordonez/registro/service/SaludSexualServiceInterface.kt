package com.crisordonez.registro.service

import com.crisordonez.registro.model.responses.SaludSexualResponse
import java.util.UUID

interface SaludSexualServiceInterface {

    fun getSaludSexual(publicId: UUID): SaludSexualResponse

    fun getTodosSaludSexual(): List<SaludSexualResponse>

}