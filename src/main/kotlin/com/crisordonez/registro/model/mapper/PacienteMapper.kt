package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.mapper.AnamnesisMapper.toResponse
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toResponse
import com.crisordonez.registro.model.mapper.SesionChatMapper.toResponse
import com.crisordonez.registro.model.requests.PacienteRequest
import com.crisordonez.registro.model.responses.PacienteResponse

import java.text.SimpleDateFormat

object PacienteMapper {

    fun PacienteRequest.toEntity(cuenta: CuentaUsuarioEntity): PacienteEntity {
        return PacienteEntity(
            nombre = this.nombre,
            fechaNacimiento = SimpleDateFormat("dd/MM/yyy").parse(this.fechaNacimiento),
            pais = this.pais,
            lenguaMaterna = this.lenguaMaterna,
            estadoCivil = this.estadoCivil,
            sexo = this.sexo,
            zonaResidencial = this.zonaResidencial,
            identificacion = this.identificacion,
            cuenta = cuenta
        )
    }

    fun PacienteRequest.toEntityUpdated(paciente: PacienteEntity): PacienteEntity {
        paciente.nombre = this.nombre
        paciente.fechaNacimiento = SimpleDateFormat("dd/MM/yyy").parse(this.fechaNacimiento)
        paciente.pais = this.pais
        paciente.lenguaMaterna = this.lenguaMaterna
        paciente.estadoCivil = this.estadoCivil
        paciente.sexo = this.sexo
        paciente.zonaResidencial = this.zonaResidencial
        paciente.identificacion = this.identificacion

        return paciente
    }

    fun PacienteEntity.toResponse(): PacienteResponse {
        return PacienteResponse(
            publicId = this.publicId,
            nombre = this.nombre,
            fechaNacimiento = SimpleDateFormat("dd/MM/yyy").format(this.fechaNacimiento),
            pais = this.pais.name,
            lenguaMaterna = this.lenguaMaterna.name,
            estadoCivil = this.estadoCivil.name,
            sexo = this.sexo.name,
            zonaResidencial = this.zonaResidencial?.name,
            identificacion = this.identificacion,
            informacionSocioeconomica = this.informacionSocioeconomica?.toResponse(),
            anamnesis = this.anamnesis?.toResponse(),
            sesionChat = this.sesionChat.map { it.toResponse() }
        )
    }
}