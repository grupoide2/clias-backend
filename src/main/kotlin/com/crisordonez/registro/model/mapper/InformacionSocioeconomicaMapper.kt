package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.InformacionSocioeconomicaEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.enums.OpcionesSiNoEnum
import com.crisordonez.registro.model.requests.InformacionSocioeconomicaRequest
import com.crisordonez.registro.model.responses.InformacionSocioeconomicaResponse

object InformacionSocioeconomicaMapper {

    fun InformacionSocioeconomicaRequest.toEntity(paciente: PacienteEntity): InformacionSocioeconomicaEntity {
        return InformacionSocioeconomicaEntity(
            instruccion = this.instruccion,
            ingresos = this.ingresos,
            ocupacion = this.ocupacion,
            trabajoRemunerado = this.trabajoRemunerado,
            recibeBono = this.recibeBono,
            dependenciaUniversitaria = this.dependenciaUniversitaria ?: OpcionesSiNoEnum.NO,
            paciente = paciente
        )
    }

    fun InformacionSocioeconomicaRequest.toEntityUpdated(info: InformacionSocioeconomicaEntity): InformacionSocioeconomicaEntity {
        info.instruccion = this.instruccion
        info.ingresos = this.ingresos
        info.ocupacion = this.ocupacion
        info.trabajoRemunerado = this.trabajoRemunerado
        info.recibeBono = this.recibeBono
        info.dependenciaUniversitaria = this.dependenciaUniversitaria ?: OpcionesSiNoEnum.NO

        return info
    }

    fun InformacionSocioeconomicaEntity.toResponse(): InformacionSocioeconomicaResponse {
        return InformacionSocioeconomicaResponse(
            publicId = this.publicId,
            instruccion = this.instruccion?.name,
            ingresos = this.ingresos?.name,
            ocupacion = this.ocupacion,
            trabajoRemunerado = this.trabajoRemunerado?.name,
            recibeBono = this.recibeBono?.name,
            dependenciaUniversitaria = this.dependenciaUniversitaria.name
        )
    }
}