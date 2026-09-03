package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toEntity
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toResponse
import com.crisordonez.registro.model.requests.InformacionSocioeconomicaRequest
import com.crisordonez.registro.model.responses.InformacionSocioeconomicaResponse
import com.crisordonez.registro.repository.InformacionSocioeconomicaRepository
import com.crisordonez.registro.repository.PacienteRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class InformacionSocioeconomicaService(
    @Autowired private val informacionSocioeconomicaRepository: InformacionSocioeconomicaRepository,
    @Autowired private val pacienteRepository: PacienteRepository
): InformacionSocioeconomicaServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun editarInfoSocioeconomica(publicId: UUID, informacion: InformacionSocioeconomicaRequest) {
        log.info("Editando informacion socioeconomica - PublicId: $publicId")
        val paciente = pacienteRepository.findByCuentaPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe la informacion del paciente solicitado")
        }

        val informacionEntity = if (paciente.informacionSocioeconomica != null) {
            informacionSocioeconomicaRepository.save(informacion.toEntityUpdated(paciente.informacionSocioeconomica!!))
        } else {
            informacionSocioeconomicaRepository.save(informacion.toEntity(paciente))
        }
        paciente.informacionSocioeconomica = informacionEntity
        pacienteRepository.save(paciente)
        log.info("Informacion socioeconomica editada correctamente")
    }

    override fun getInfoSocioeconomica(publicId: UUID): InformacionSocioeconomicaResponse? {
        log.info("Consultando informacion socioeconomica - PublicId: $publicId")
        val paciente = pacienteRepository.findByCuentaPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe la informacion del paciente solicitado")
        }
        val informacion = paciente.informacionSocioeconomica
        log.info("Informacion consultada correctamente")
        return informacion?.toResponse()
    }

    override fun getTodosInfo(): List<InformacionSocioeconomicaResponse> {
        log.info("Consultando todos los registros de informacion socioeconomica")
        val infos = informacionSocioeconomicaRepository.findAll().map { it.toResponse() }
        log.info("Registros consultados correctamente - Total: ${infos.size}")
        return infos
    }
    override fun existeFichaSocioeconomica(publicId: UUID): Boolean {
        return informacionSocioeconomicaRepository.existsByCuentaUsuarioPublicId(publicId)
    }

}