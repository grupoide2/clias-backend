// src/main/kotlin/com/crisordonez/registro/service/MedicoService.kt
package com.crisordonez.registro.service

import com.crisordonez.registro.model.mapper.MedicoMapper.toEntity
import com.crisordonez.registro.model.mapper.MedicoMapper.toResponse
import com.crisordonez.registro.model.requests.MedicoRequest
import com.crisordonez.registro.model.responses.MedicoResponse
import com.crisordonez.registro.repository.MedicoRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import java.util.UUID

@Service
class MedicoService(
    private val repo: MedicoRepository,
    private val encoder: PasswordEncoder
) {

    fun crearMedico(dto: MedicoRequest): MedicoResponse {
        val entity = dto.toEntity(encoder)
        val saved  = repo.save(entity)
        return saved.toResponse()
    }

    fun listarMedicos(): List<MedicoResponse> =
        repo.findAll().map { it.toResponse() }

    fun obtenerMedico(id: String): MedicoResponse {
        val medico = repo.findByPublicId(UUID.fromString(id))
            ?: throw NoSuchElementException("Médico id=$id no encontrado")
        return medico.toResponse()
    }

    fun actualizarMedico(id: String, dto: MedicoRequest): MedicoResponse {
        val medico = repo.findByPublicId(UUID.fromString(id))
            ?: throw NoSuchElementException("Médico id=$id no encontrado")
        medico.apply {
            usuario         = dto.usuario
            contrasena      = encoder.encode(dto.contrasena)
            nombre          = dto.nombre
            correo          = dto.correo
            especializacion = dto.especializacion
            sexo            = dto.sexo
            nRegistro       = dto.nRegistro
        }
        return repo.save(medico).toResponse()
    }


    fun eliminarMedico(id: String) {
        val medico = repo.findByPublicId(UUID.fromString(id))
            ?: throw NoSuchElementException("Médico id=$id no encontrado")
        repo.delete(medico)
    }

    fun authenticateAndGet(usuario: String, rawPassword: String): MedicoResponse? {
        val medicoEntity = repo.findByUsuario(usuario)
        if (medicoEntity != null && encoder.matches(rawPassword, medicoEntity.contrasena)) {
            return medicoEntity.toResponse()
        }
        return null
    }

    fun obtenerMedicoPorId(id: Long): MedicoResponse {
        val medico = repo.findById(id)
            .orElseThrow { NoSuchElementException("Médico id=$id no encontrado") }
        return medico.toResponse()
    }
}
