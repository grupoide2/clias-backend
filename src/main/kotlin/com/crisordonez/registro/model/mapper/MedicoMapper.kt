package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.MedicoEntity
import com.crisordonez.registro.model.requests.MedicoRequest
import com.crisordonez.registro.model.responses.MedicoResponse
import org.springframework.security.crypto.password.PasswordEncoder
import com.crisordonez.registro.model.mapper.EvolucionMapper.toResponse

object MedicoMapper {

    fun MedicoRequest.toEntity(encoder: PasswordEncoder): MedicoEntity {
        return MedicoEntity(
            usuario         = this.usuario,
            contrasena      = encoder.encode(this.contrasena),
            nombre          = this.nombre,
            correo          = this.correo,
            especializacion = this.especializacion,
            sexo            = this.sexo,
            nRegistro       = this.nRegistro
        )
    }

    fun MedicoEntity.toResponse(): MedicoResponse {
        return MedicoResponse(
            publicId        = this.publicId,
            usuario         = this.usuario,
            nombre          = this.nombre,
            correo          = this.correo,
            especializacion = this.especializacion,
            sexo            = this.sexo,
            nRegistro       = this.nRegistro ?: "",
            evoluciones     = this.evoluciones.map { it.toResponse() }
        )
    }
}
