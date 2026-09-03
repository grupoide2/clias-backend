package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.BadRequestException
import com.crisordonez.registro.model.errors.ConflictException
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.RangoTiempoExamenMapper.applyUpdate
import com.crisordonez.registro.model.mapper.RangoTiempoExamenMapper.toEntity
import com.crisordonez.registro.model.mapper.RangoTiempoExamenMapper.toResponse
import com.crisordonez.registro.model.requests.RangoTiempoExamenRequest
import com.crisordonez.registro.model.responses.RangoTiempoExamenResponse
import com.crisordonez.registro.repository.RangoTiempoExamenRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RangoTiempoExamenService(
    private val rangoRepository: RangoTiempoExamenRepository
) : RangoTiempoExamenServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun listar(pregunta: String?, soloActivos: Boolean): List<RangoTiempoExamenResponse> {
        val filtro = pregunta?.trim()?.uppercase()?.takeIf { it.isNotEmpty() }
        val base = when {
            filtro != null && soloActivos ->
                rangoRepository.findAllByPreguntaAndActivoTrueOrderByOrdenAscEtiquetaAsc(filtro)
            filtro != null ->
                rangoRepository.findAllByPreguntaOrderByOrdenAscEtiquetaAsc(filtro)
            soloActivos ->
                rangoRepository.findAllByActivoTrueOrderByOrdenAscEtiquetaAsc()
            else ->
                rangoRepository.findAllByOrderByOrdenAscEtiquetaAsc()
        }
        return base.map { it.toResponse() }
    }

    override fun crear(request: RangoTiempoExamenRequest): RangoTiempoExamenResponse {
        val codigo = request.codigo.trim().uppercase()
        if (rangoRepository.existsByCodigo(codigo)) {
            throw ConflictException("El rango de tiempo '$codigo' ya existe")
        }
        val guardado = rangoRepository.save(request.toEntity())
        log.info("Rango de tiempo creado: ${guardado.codigo}")
        return guardado.toResponse()
    }

    override fun actualizar(publicId: UUID, request: RangoTiempoExamenRequest): RangoTiempoExamenResponse {
        val rango = rangoRepository.findByPublicId(publicId)
            .orElseThrow { NotFoundException("El rango de tiempo no existe") }
        val actualizado = rangoRepository.save(rango.applyUpdate(request))
        log.info("Rango de tiempo actualizado: ${actualizado.codigo}")
        return actualizado.toResponse()
    }

    override fun eliminar(publicId: UUID) {
        val rango = rangoRepository.findByPublicId(publicId)
            .orElseThrow { NotFoundException("El rango de tiempo no existe") }
        rangoRepository.delete(rango)
        log.info("Rango de tiempo eliminado: ${rango.codigo}")
    }

    override fun validarCodigoActivo(codigo: String) {
        val rango = rangoRepository.findByCodigo(codigo.trim().uppercase())
            .orElseThrow { BadRequestException("El rango de tiempo '$codigo' no es válido") }
        if (!rango.activo) throw BadRequestException("El rango de tiempo '$codigo' está inactivo")
    }
}
