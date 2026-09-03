package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.DispositivoAppUsuarioEntity
import com.crisordonez.registro.model.entities.NotificacionEntity
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import com.crisordonez.registro.model.mapper.NotificacionMapper.toEntity
import com.crisordonez.registro.model.mapper.NotificacionMapper.toResponse
import com.crisordonez.registro.model.mapper.NotificacionProgramadaMapper.toEntity
import com.crisordonez.registro.repository.NotificacionProgramadaRepository
import com.crisordonez.registro.repository.NotificacionRepository
import com.crisordonez.registro.model.requests.NotificacionProgramadaRequest
import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.model.responses.NotificacionResponse
import com.crisordonez.registro.repository.CuentaUsuarioRepository
import com.crisordonez.registro.repository.DispositivoAppUsuarioRepository
import com.crisordonez.registro.repository.EncuestaSusRepository
import com.crisordonez.registro.repository.ExamenVphRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
import java.time.Duration


@Service
class NotificacionService(
    private val notificacionRepository: NotificacionRepository,
    private val notificacionProgramadaRepository: NotificacionProgramadaRepository,
    private val cuentaUsuarioRepository: CuentaUsuarioRepository,
    private val pushNotificacionService: PushNotificacionServiceInterface,
    private val dispositivoAppUsuarioRepository: DispositivoAppUsuarioRepository,
    private val examenVphRepository: ExamenVphRepository,
    private val encuestaSusRepository: EncuestaSusRepository,
) : NotificacionServiceInterface {

    private val logger = LoggerFactory.getLogger(NotificacionService::class.java)

    @Transactional
    override fun crearNotificacion(request: NotificacionRequest): NotificacionResponse {
        val cuentaUsuario = cuentaUsuarioRepository.findByPublicId(request.cuentaUsuarioPublicId)
            .orElseThrow { IllegalArgumentException("CuentaUsuario no encontrado") }

        val notificacion = request.toEntity(cuentaUsuario)
        val guardada = notificacionRepository.save(notificacion)

        /* *
         * Enviar Notificación Push a TODOS los dispositivos válidos del usuario
         */
        val notificacionResponse = guardada.toResponse()
        pushNotificacionService.enviarPushFCM(cuentaUsuario.publicId, notificacionResponse)


        /**
         * Proceso de crear un notificación programada para la notificación de tipo RESULTADO
         */
        if (request.tipoNotificacion == TipoNotificacionEnum.RESULTADO) {
            val yaExiste = notificacionProgramadaRepository
                .existsByCuentaUsuarioAndTipoNotificacionAndProgramacionActivaTrue(
                    cuentaUsuario,
                    TipoNotificacionEnum.RESULTADO
                )

            if (!yaExiste) {
                logger.info("[Programada] Iniciando programación recurrente para notificación de RESULTADO")

                val ahora = LocalDateTime.now()
                val requestProgramada = NotificacionProgramadaRequest(
                    notificacionPublicId = UUID.randomUUID(),
                    tipoNotificacion = notificacion.tipo_notificacion,
                    titulo = notificacion.titulo,
                    mensaje = notificacion.mensaje,
                    tipoAccion = notificacion.tipo_accion,
                    accion = notificacion.accion,
                    proxFecha = ahora.plusDays(1), //Al siguiente día de haber recibido el resultado
                    limiteFecha = ahora.plusDays(14)
                )
                crearNotificacionProgramada(request, requestProgramada)

            } else {
                logger.info("[Programada] Ya existe una notificación programada de tipo RESULTADO para ${cuentaUsuario.id}, no se crea otra.")
            }
        }
        return guardada.toResponse()
    }

    override fun obtenerHistorialNotificaciones(cuentaUsuarioPublicId: UUID): List<NotificacionResponse> {
        val notificaciones =
            notificacionRepository.findAllByCuentaUsuarioPublicIdOrderByFechaCreacionDesc(cuentaUsuarioPublicId)
        return notificaciones.map { it.toResponse() }
    }

    override fun marcarNotificacionComoLeida(publicId: UUID) {
        val notificacion = notificacionRepository.findByPublicId(publicId)
            .orElseThrow { IllegalArgumentException("Notificación no encontrada para ser marcada como Leída") }

        notificacion.notificacion_leida = true
        notificacionRepository.save(notificacion)
    }
    override fun desactivarRecordatorioNoEntregaDispositivo(cuentaUsuarioPublicId: UUID) {
        val notificacion = notificacionProgramadaRepository
            .findActivaByCuentaUsuarioPublicIdAndTipoNotificacion(
                cuentaUsuarioPublicId,
                TipoNotificacionEnum.RECORDATORIO_NO_ENTREGA_DISPOSITIVO
            )
            .orElse(null)

        if (notificacion != null && notificacion.programacionActiva) {
            notificacionProgramadaRepository.updateActivaById(false, notificacion.id)
            println("[OK] Notificación desactivada.")
        } else {
            println("[!] Ya estaba desactivada o no existe. No se realizó ningún cambio.")
        }
    }

    @Transactional
    override fun desactivarRecordatorioNoExamen(cuentaUsuarioPublicId: UUID) {
        val desactivadas = notificacionProgramadaRepository
            .desactivarActivasByCuentaUsuarioPublicIdAndTipo(
                cuentaUsuarioPublicId,
                TipoNotificacionEnum.RECORDATORIO_NO_EXAMEN
            )
        logger.info("Recordatorio(s) 'realízate el examen' desactivado(s) para $cuentaUsuarioPublicId: $desactivadas")
    }


    @Transactional
    override fun crearNotificacionProgramada(
        requestNotificacion: NotificacionRequest,
        requestProgramada: NotificacionProgramadaRequest
    ): NotificacionResponse {
        val cuentaUsuario = cuentaUsuarioRepository.findByPublicId(requestNotificacion.cuentaUsuarioPublicId)
            .orElseThrow { IllegalArgumentException("CuentaUsuario no encontrado - creación programada") }

        val programada = requestProgramada.toEntity(cuentaUsuario)
        val guardada = notificacionProgramadaRepository.save(programada)

        logger.info("[Programada] Notificación programada para ${cuentaUsuario.id} a partir del ${programada.proxFecha}")
        return NotificacionResponse(
            publicId = guardada.publicId,
            cuentaUsuarioPublicId = cuentaUsuario.publicId,
            titulo = guardada.titulo,
            mensaje = guardada.mensaje,
            tipoNotificacion = guardada.tipoNotificacion,
            tipoAccion = guardada.tipoAccion,
            accion = guardada.accion,
            fechaCreacion = guardada.proxFecha, // como es plantilla, su "creación" es el inicio
            notificacionLeida = false // solo se marca en instancias reales
        )
    }

    @Transactional
    override fun procesarNotificacionesProgramadas() {
        val ahora = LocalDateTime.now()
        val pendientes = notificacionProgramadaRepository.findAllByProxFechaBeforeAndProgramacionActivaIsTrue(ahora)

        pendientes.forEach { prog ->
            val cuentaUsuario = prog.cuentaUsuario
            if (prog.tipoNotificacion == TipoNotificacionEnum.RECORDATORIO_NO_EXAMEN) {
                val paciente = prog.cuentaUsuario.paciente
                if (paciente != null && examenVphRepository.existsExamenByPacienteId(paciente.id!!)) {
                    logger.info("Paciente ${paciente.id} ya tiene examen, se cancela la notificación programada (${prog.publicId})")
                    prog.programacionActiva = false
                    notificacionProgramadaRepository.save(prog)
                    return@forEach
                }
            }
            if (prog.tipoNotificacion == TipoNotificacionEnum.RECORDATORIO_NO_ENTREGA_DISPOSITIVO) {
                val cuentaUsuarioPublicId = prog.cuentaUsuario.publicId
                if (examenVphRepository.existsExamenEntregadoByCuentaUsuarioPublicId(cuentaUsuarioPublicId)) {
                    logger.info("El examen ya fue entregado (contenido != null) para cuenta $cuentaUsuarioPublicId, desactivando notificación programada.")
                    prog.programacionActiva = false
                    notificacionProgramadaRepository.save(prog)
                    return@forEach
                }
            }

            if (prog.tipoNotificacion == TipoNotificacionEnum.RESULTADO) {
                val cuentaUsuarioPublicId = prog.cuentaUsuario.publicId
                val yaRespondioEncuesta = encuestaSusRepository.existsByCuentaUsuarioPublicId(cuentaUsuarioPublicId)
                if (yaRespondioEncuesta) {
                    logger.info("Usuario ${cuentaUsuario.id} ya respondió la encuesta SUS, se desactiva notificación programada (${prog.publicId})")
                    prog.programacionActiva = false
                    notificacionProgramadaRepository.save(prog)
                    return@forEach
                }
            }

            // 1. Crear notificación real
            crearNotificacion(
                NotificacionRequest(
                    cuentaUsuarioPublicId = cuentaUsuario.publicId,
                    tipoNotificacion = prog.tipoNotificacion,
                    titulo = prog.titulo,
                    mensaje = prog.mensaje,
                    tipoAccion = prog.tipoAccion,
                    accion = prog.accion
                )
            )


            // 1. Calcula cuántos días han pasado desde la fecha de inicio
            val diasTranscurridos = Duration.between(prog.fechaInicio, ahora).toDays()

            // 2. Decide con qué frecuencia se debe volver a enviar la notificación
            val nuevoIntervaloEnDias = when {
                diasTranscurridos < 30 -> 3L  // Durante el primer mes, enviar cada 3 días
                diasTranscurridos < 60 -> 7L  // Segundo mes, enviar cada 7 días
                else -> null                  // Después de 2 meses, dejar de enviar
            }

            // 3. Verifica si ya se cumplió el tiempo límite o si ya debe dejar de enviarse
            if (nuevoIntervaloEnDias == null || (prog.limiteFecha != null && ahora.isAfter(prog.limiteFecha))) {
                prog.programacionActiva = false
            } else {
                prog.proxFecha = ahora.plusDays(nuevoIntervaloEnDias)
            }

            notificacionProgramadaRepository.save(prog)
            logger.info("[Programada] Notificación enviada y reprogramada (${prog.titulo}) para ${cuentaUsuario.id}, comenzando en [${prog.fechaInicio}] -> [${prog.proxFecha}]")
        }
    }


}
