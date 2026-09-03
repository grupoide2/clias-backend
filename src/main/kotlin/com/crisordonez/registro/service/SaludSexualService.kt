package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.SaludSexualMapper.toResponse
import com.crisordonez.registro.model.responses.SaludSexualResponse
import com.crisordonez.registro.repository.SaludSexualRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SaludSexualService(@Autowired private val saludSexualRepository: SaludSexualRepository): SaludSexualServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun getSaludSexual(publicId: UUID): SaludSexualResponse {
        log.info("Consultando salud sexual - PublicId: $publicId")
        val registro = saludSexualRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe el registro solicitado")
        }
        log.info("Salud sexual consuiltada correctamente")
        return registro.toResponse()
    }

    override fun getTodosSaludSexual(): List<SaludSexualResponse> {
        log.info("Consultados todos los registro de salud sexual")
        val registros = saludSexualRepository.findAll().map { it.toResponse() }
        log.info("Registros consultados - Total: ${registros.size}")
        return registros
    }
}