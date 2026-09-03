package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.EvolucionEntity
import com.crisordonez.registro.model.entities.ExamenVphEntity
import com.crisordonez.registro.model.requests.EvolucionRequest
import com.crisordonez.registro.model.responses.EvolucionResponse
import java.time.LocalDateTime

object EvolucionMapper {

    fun EvolucionRequest.toEntity(examen: ExamenVphEntity): EvolucionEntity {
        return EvolucionEntity(
            temperatura = this.temperatura,
            pulso = this.pulso,
            talla = this.talla,
            peso = this.peso,
            examenVph = examen,
            fecha = this.fecha
        )
    }

    fun EvolucionEntity.toResponse(): EvolucionResponse {
        return EvolucionResponse(
            publicId = this.publicId,
            temperatura = this.temperatura,
            pulso = this.pulso,
            talla = this.talla,
            peso = this.peso,
            fecha = this.fecha
        )
    }
}
