package com.crisordonez.registro.model.responses

import java.util.UUID

data class PacienteResponse(

    val publicId: UUID,

    val nombre: String,

    val fechaNacimiento: String,

    val pais: String,

    val lenguaMaterna: String,

    val estadoCivil: String,

    val sexo: String,

    val zonaResidencial: String? = null,

    val identificacion: String? = null,

    val informacionSocioeconomica: InformacionSocioeconomicaResponse? = null,

    val anamnesis: AnamnesisResponse? = null,

    val sesionChat: List<SesionChatResponse> = emptyList()


)
