package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.ConflictException
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.OcupacionMapper.applyUpdate
import com.crisordonez.registro.model.mapper.OcupacionMapper.toEntity
import com.crisordonez.registro.model.mapper.OcupacionMapper.toResponse
import com.crisordonez.registro.model.requests.OcupacionRequest
import com.crisordonez.registro.model.responses.OcupacionResponse
import com.crisordonez.registro.repository.OcupacionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OcupacionService(
    private val ocupacionRepository: OcupacionRepository
) : OcupacionServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun listar(soloActivas: Boolean, soloUniversidad: Boolean?): List<OcupacionResponse> {
        val base = if (soloActivas) {
            ocupacionRepository.findAllByActivoTrueOrderByOrdenAscNombreAsc()
        } else {
            ocupacionRepository.findAllByOrderByOrdenAscNombreAsc()
        }
        return base
            .filter { soloUniversidad == null || it.soloUniversidad == soloUniversidad }
            .map { it.toResponse() }
    }

    override fun crear(request: OcupacionRequest): OcupacionResponse {
        val nombre = request.nombre.trim()
        if (ocupacionRepository.existsByNombreIgnoreCase(nombre)) {
            throw ConflictException("La ocupación '$nombre' ya existe")
        }
        val guardada = ocupacionRepository.save(request.toEntity())
        log.info("Ocupación creada: ${guardada.nombre}")
        return guardada.toResponse()
    }

    override fun actualizar(publicId: UUID, request: OcupacionRequest): OcupacionResponse {
        val ocupacion = ocupacionRepository.findByPublicId(publicId)
            .orElseThrow { NotFoundException("La ocupación no existe") }

        val nombre = request.nombre.trim()
        ocupacionRepository.findByNombreIgnoreCase(nombre).ifPresent {
            if (it.publicId != publicId) throw ConflictException("La ocupación '$nombre' ya existe")
        }

        val actualizada = ocupacionRepository.save(ocupacion.applyUpdate(request))
        log.info("Ocupación actualizada: ${actualizada.nombre}")
        return actualizada.toResponse()
    }

    override fun eliminar(publicId: UUID) {
        val ocupacion = ocupacionRepository.findByPublicId(publicId)
            .orElseThrow { NotFoundException("La ocupación no existe") }
        ocupacionRepository.delete(ocupacion)
        log.info("Ocupación eliminada: ${ocupacion.nombre}")
    }
}
