package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.EvolucionMapper.toEntity
import com.crisordonez.registro.model.mapper.EvolucionMapper.toResponse
import com.crisordonez.registro.model.requests.EvolucionRequest
import com.crisordonez.registro.model.responses.EvolucionResponse
import com.crisordonez.registro.repository.EvolucionRepository
import com.crisordonez.registro.repository.ExamenVphRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EvolucionService(
    @Autowired private val evolucionRepository: EvolucionRepository,
    @Autowired private val examenVphRepository: ExamenVphRepository
) : EvolucionServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun crearEvolucion(publicId: String, evolucion: EvolucionRequest) {
        log.info("Creando evolucion")
        val prueba = examenVphRepository.findByDispositivo(publicId).orElseThrow {
            throw NotFoundException("No existe una prueba relacionada al dispositivo de referencia")
        }
        val evolucionEntity = evolucionRepository.save(evolucion.toEntity(prueba))
        prueba.evolucion.add(evolucionEntity)
        examenVphRepository.save(prueba)
        log.info("Evolucion creada correctamente")
    }

    override fun getEvolucion(publicId: UUID): EvolucionResponse {
        log.info("Consultando evolucion - PublicId: $publicId")
        val evolucion = evolucionRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe el registro solicitado")
        }
        log.info("Evolucion consultada correctamente")
        return evolucion.toResponse()
    }

    override fun getTodasEvoluciones(): List<EvolucionResponse> {
        log.info("Consultando todos los registros de evolucion")
        val registros = evolucionRepository.findAll().map { it.toResponse() }
        log.info("Registros consultados - Total: ${registros.size}")
        return registros
    }

    override fun eliminarEvolucion(publicId: UUID) {
        log.info("Eliminando evolucion - PublicId: $publicId")
        val evolucion = evolucionRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe el registro solicitado")
        }
        evolucionRepository.delete(evolucion)
    }

}