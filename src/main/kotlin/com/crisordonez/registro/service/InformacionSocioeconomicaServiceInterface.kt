package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.InformacionSocioeconomicaRequest
import com.crisordonez.registro.model.responses.InformacionSocioeconomicaResponse
import java.util.UUID

interface InformacionSocioeconomicaServiceInterface {

    fun editarInfoSocioeconomica(publicId: UUID, informacion: InformacionSocioeconomicaRequest)

    fun getInfoSocioeconomica(publicId: UUID): InformacionSocioeconomicaResponse?

    fun getTodosInfo(): List<InformacionSocioeconomicaResponse>
    fun existeFichaSocioeconomica(publicId: UUID): Boolean


}