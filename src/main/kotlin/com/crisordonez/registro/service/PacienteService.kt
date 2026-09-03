// src/main/kotlin/com/crisordonez/registro/service/PacienteService.kt
package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.ConflictException
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.DispositivoRegistradoMapper.toEntity
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toEntity
import com.crisordonez.registro.model.mapper.InformacionSocioeconomicaMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.PacienteMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.PacienteMapper.toResponse
import com.crisordonez.registro.model.requests.DispositivoRegistradoRequest
import com.crisordonez.registro.model.requests.PacienteRequest
import com.crisordonez.registro.model.responses.EstadoAutomuestreoResponse
import com.crisordonez.registro.model.responses.PacienteResponse
import com.crisordonez.registro.repository.DispositivoRegistradoRepository
import com.crisordonez.registro.repository.ExamenVphRepository
import com.crisordonez.registro.repository.InformacionSocioeconomicaRepository
import com.crisordonez.registro.repository.PacienteRepository
import com.crisordonez.registro.model.entities.DispositivoRegistradoEntity
import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import com.crisordonez.registro.model.requests.NotificacionProgramadaRequest
import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.utils.MensajesNotificacion


import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class PacienteService(
    @Autowired private val pacienteRepository: PacienteRepository,

    @Autowired private val dispositivoRegistradoRepository: DispositivoRegistradoRepository,

    @Autowired private val informacionSocioeconomicaRepository: InformacionSocioeconomicaRepository,
    @Autowired private val examenVphRepository: ExamenVphRepository,
    private val notificacionService: NotificacionServiceInterface

    ): PacienteServiceInterface {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun editarPaciente(publicId: UUID, paciente: PacienteRequest) {
        log.info("Editando informacion paciente - PublicId: $publicId")
        val pacienteExistente = pacienteRepository.findByCuentaPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe la informacion del paciente solicitado")
        }

        val pacienteActualizado = paciente.toEntityUpdated(pacienteExistente)

        if (paciente.infoSocioeconomica != null) {
            val informacionEntity = if (pacienteActualizado.informacionSocioeconomica != null) {
                informacionSocioeconomicaRepository.save(paciente.infoSocioeconomica.toEntityUpdated(pacienteActualizado.informacionSocioeconomica!!))
            } else {
                informacionSocioeconomicaRepository.save(paciente.infoSocioeconomica.toEntity(pacienteActualizado))
            }
            pacienteActualizado.informacionSocioeconomica = informacionEntity
        }

        pacienteRepository.save(pacienteActualizado)
        log.info("Informacion del paciente editada correctamente")
    }

    override fun getPaciente(publicId: UUID): PacienteResponse {
        log.info("Consultando informacion paciente - PublicId: $publicId")
        val paciente = pacienteRepository.findByCuentaPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe la informacion del paciente solicitado")
        }

        log.info("Informacion consultada correctamente")
        return paciente.toResponse()
    }

    override fun getTodosPacientes(): List<PacienteResponse> {
        log.info("Consultando informacion de los pacientes")
        val pacientes = pacienteRepository.findAll().map { it.toResponse() }
        log.info("Informacion consultada correctamente - Total: ${pacientes.size} registros")
        return pacientes
    }

    override fun registrarDispositivo(
        publicId: UUID,
        dispositivo: DispositivoRegistradoRequest
    ): String {
        println("UUID Recibido para el registro del dispositivo: $publicId")
        val paciente = pacienteRepository.findByCuentaPublicId(publicId)
            .orElseThrow { NotFoundException("Paciente no encontrado") }

        // Un mismo dispositivo no puede registrarse dos veces.
        val codigo = dispositivo.dispositivo.trim()
        dispositivoRegistradoRepository.findByDispositivo(codigo).orElse(null)?.let { existente ->
            if (existente.pacienteId != paciente.id) {
                throw ConflictException("Este dispositivo ya fue registrado por otra paciente")
            }
            log.info("Dispositivo $codigo ya registrado por la misma paciente; no se duplica")
            return existente.dispositivo
        }

        val dispEnt: DispositivoRegistradoEntity =
            dispositivo.toEntity(paciente)
        val guardado: DispositivoRegistradoEntity =
            dispositivoRegistradoRepository.save(dispEnt)

        paciente.dispositivos.add(guardado)
        pacienteRepository.save(paciente)

        // Si la paciente ya completó un automuestreo, no se re-arma el recordatorio "realízate el examen".
        if (paciente.id != null && examenVphRepository.existsExamenByPacienteId(paciente.id!!)) {
            log.info("Paciente ${paciente.id} ya tiene examen; no se crea RECORDATORIO_NO_EXAMEN")
            return guardado.dispositivo
        }

        // 🔔 Agregar notificación programada
        val now = LocalDateTime.now()

        val request = NotificacionProgramadaRequest(
            notificacionPublicId = UUID.randomUUID(), // no se usa realmente
            tipoNotificacion = TipoNotificacionEnum.RECORDATORIO_NO_EXAMEN,
            titulo = MensajesNotificacion.NOT_TITULO_NO_EXAMEN,
            mensaje = MensajesNotificacion.NOT_MENSAJE_NO_EXAMEN,
            tipoAccion = TipoAccionNotificacionEnum.valueOf(MensajesNotificacion.NOT_TIPO_ACCION_NO_EXAMEN),
            accion = MensajesNotificacion.NOT_ACCION_NO_EXAMEN,
            proxFecha = now.plusDays(3), // Próxima vez en 3 días
            limiteFecha = now.plusDays(59) // Máximo hasta 2 meses
        )

        notificacionService.crearNotificacionProgramada(
            requestNotificacion = NotificacionRequest(
                cuentaUsuarioPublicId = publicId,
                tipoNotificacion = request.tipoNotificacion,
                titulo = request.titulo,
                mensaje = request.mensaje,
                tipoAccion = request.tipoAccion,
                accion = request.accion
            ),
            requestProgramada = request
        )


        return guardado.dispositivo
    }

    override fun findByDispositivo(codigo: String): PacienteResponse? {
        log.info("Buscando paciente por dispositivo: $codigo")
        val disp = dispositivoRegistradoRepository.findByDispositivo(codigo)
            .orElseThrow { Exception("Dispositivo no registrado: $codigo") }
        val pacienteEnt = disp.paciente
        return pacienteEnt.toResponse()
    }

    override fun obtenerPublicIdPorIdInterno(id: Long): UUID {
        return pacienteRepository.findPublicIdById(id)
            ?: throw NotFoundException("No se encontró el paciente con ese identificador_i.")
    }

    override fun estadoAutomuestreo(publicId: UUID): EstadoAutomuestreoResponse {
        val paciente = pacienteRepository.findByCuentaPublicId(publicId)
            .orElseThrow { NotFoundException("Paciente no encontrado") }
        val pacienteId = paciente.id!!
        val completado = examenVphRepository.existsExamenByPacienteId(pacienteId)
        val codigo = if (completado)
            examenVphRepository.findDispositivosByPacienteIdOrderByIdDesc(pacienteId).firstOrNull()
        else null
        return EstadoAutomuestreoResponse(completado = completado, codigoDispositivo = codigo)
    }


}