package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.EstadoCivilEnum
import com.crisordonez.registro.model.enums.IdiomaEnum
import com.crisordonez.registro.model.enums.PaisEnum
import com.crisordonez.registro.model.enums.SexoEnum
import com.crisordonez.registro.model.enums.ZonaResidencialEnum
import jakarta.validation.constraints.NotNull

data class PacienteRequest(

    @field:NotNull(message = "El nombre es requerido")
    val nombre: String,

    @field:NotNull(message = "La fecha de nacimiento es requerida")
    val fechaNacimiento: String,

    @field:NotNull(message = "El país es requerido")
    val pais: PaisEnum,

    @field:NotNull(message = "El idioma es requerido")
    val lenguaMaterna: IdiomaEnum,

    @field:NotNull(message = "El estado civil es requerido")
    val estadoCivil: EstadoCivilEnum,

    @field:NotNull(message = "El sexo es requerido")
    val sexo: SexoEnum,

    val zonaResidencial: ZonaResidencialEnum? = null,

    val identificacion: String? = null,

    val infoSocioeconomica: InformacionSocioeconomicaRequest? = null

)
