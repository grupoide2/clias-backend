package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.DispositivoRegistradoEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.requests.DispositivoRegistradoRequest
import com.crisordonez.registro.model.responses.DispositivoRegistradoResponse

object DispositivoRegistradoMapper {

    fun DispositivoRegistradoRequest.toEntity(paciente: PacienteEntity): DispositivoRegistradoEntity {
        return DispositivoRegistradoEntity(
            dispositivo = this.dispositivo,
            pacienteId = paciente.id
                ?: throw IllegalStateException("El paciente debe tener un ID antes de registrar un dispositivo"),
            paciente = paciente
        )
    }


    fun DispositivoRegistradoEntity.toResponse(): DispositivoRegistradoResponse {
        return DispositivoRegistradoResponse(
            dispositivo           = this.dispositivo,
            pacienteId            = this.paciente.id
                ?: throw IllegalStateException("El paciente asociado no tiene un ID"),
            pacienteNombre        = this.paciente.nombre,
            publicId              = this.publicId,
            fechaRegistro         = this.fechaRegistro,
            cuentaUsuarioPublicId = null
        )
    }
}