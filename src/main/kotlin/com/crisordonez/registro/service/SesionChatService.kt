package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.entities.DispositivoRegistradoEntity
import com.crisordonez.registro.model.entities.ExamenVphEntity
import com.crisordonez.registro.model.entities.InformacionSocioeconomicaEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.entities.SesionChatEntity
import com.crisordonez.registro.model.enums.EstadoCivilEnum
import com.crisordonez.registro.model.enums.IdiomaEnum
import com.crisordonez.registro.model.enums.IngresoEnum
import com.crisordonez.registro.model.enums.OpcionesSiNoEnum
import com.crisordonez.registro.model.enums.PaisEnum
import com.crisordonez.registro.model.enums.SexoEnum
import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import com.crisordonez.registro.model.enums.ZonaResidencialEnum
import com.crisordonez.registro.model.errors.BadRequestException
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.ExamenVphMapper.toEntity
import com.crisordonez.registro.model.mapper.ExamenVphMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.SaludSexualMapper.toEntity
import com.crisordonez.registro.model.mapper.SaludSexualMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.SesionChatMapper.toEntity
import com.crisordonez.registro.model.mapper.SesionChatMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.SesionChatMapper.toResponse
import com.crisordonez.registro.model.requests.FolletoRequest
import com.crisordonez.registro.model.requests.NotificacionProgramadaRequest
import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.model.requests.SesionChatRequest
import com.crisordonez.registro.model.requests.SesionChatbotFinRequest
import com.crisordonez.registro.model.requests.SesionChatbotInicioRequest
import com.crisordonez.registro.model.responses.SesionChatResponse
import com.crisordonez.registro.repository.*
import com.crisordonez.registro.utils.MensajesNotificacion.NOT_ACCION_NO_ENTREGA
import com.crisordonez.registro.utils.MensajesNotificacion.NOT_MENSAJE_NO_ENTREGA
import com.crisordonez.registro.utils.MensajesNotificacion.NOT_TIPO_ACCION_NO_ENTREGA
import com.crisordonez.registro.utils.MensajesNotificacion.NOT_TITULO_NO_ENTREGA
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class SesionChatService(
    private val sesionChatRepository: SesionChatRepository,
    private val saludSexualRepository: SaludSexualRepository,
    private val examenVphRepository: ExamenVphRepository,
    private val pacienteRepository: PacienteRepository,
    private val cuentaUsuarioRepository: CuentaUsuarioRepository,
    private val dispositivoRegistradoRepository: DispositivoRegistradoRepository,
    private val notificacionService: NotificacionService,
    private val passwordEncoder: PasswordEncoder,
    private val metricaUsoService: MetricaUsoServiceInterface,
    private val rangoTiempoExamenService: RangoTiempoExamenServiceInterface,
    private val informacionSocioeconomicaRepository: InformacionSocioeconomicaRepository,
): SesionChatServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun crearSesionChat(sesion: SesionChatRequest) {
        log.info("Creando sesion de chat - Usuario: ${sesion.cuentaPublicId}")
        val paciente = pacienteRepository.findByCuentaPublicId(sesion.cuentaPublicId).orElseThrow{
            throw NotFoundException("La cuenta de usuario solicitada no existe")
        }
        if (sesion.examenVph != null) {
            rangoTiempoExamenService.validarCodigoActivo(sesion.examenVph.saludSexual.ultimoExamenPap)
            rangoTiempoExamenService.validarCodigoActivo(sesion.examenVph.saludSexual.tiempoPruebaVph)
            var sesionChat = sesionChatRepository.findByExamenVphDispositivo(sesion.examenVph.dispositivo)

            // Solo se registra/actualiza el automuestreo sobre una sesión EXAMEN de la app.
            if (sesionChat != null && (sesionChat.tipo != "EXAMEN" || sesionChat.origen != "APP")) {
                throw BadRequestException(
                    "La sesión de chat del dispositivo no es de tipo EXAMEN/APP; no se puede registrar el automuestreo"
                )
            }

            if (sesionChat == null) {
                sesionChat = sesionChatRepository.save(sesion.toEntity(paciente))
            }

            if (sesionChat.examenVph != null) {
                saludSexualRepository.save(sesion.examenVph.saludSexual.toEntityUpdated(sesionChat.examenVph!!.saludSexual))
                examenVphRepository.save(sesion.examenVph.toEntityUpdated(sesionChat.examenVph!!))
                sesionChatRepository.save(sesion.toEntityUpdated(sesionChat))
            } else {
                val saludSexual = saludSexualRepository.save(sesion.examenVph.saludSexual.toEntity())
                val prueba = examenVphRepository.save(sesion.examenVph.toEntity(sesionChat, saludSexual))
                saludSexual.examenVph = prueba
                sesionChat.examenVph = prueba
                sesionChatRepository.save(sesionChat)
                saludSexualRepository.save(saludSexual)
                paciente.sesionChat.add(sesionChat)
                pacienteRepository.save(paciente)
            }
            //REGISTRO DE NOTIFICACIÓN A EJECUTARSE DENTRO DE 7 DÍAS, SI ES QUE NO SE DETECTA QUE ENTREGÓ EL DISPOSITIVO
            val cuentaUsuario = paciente.cuenta

            // La paciente ya completó el automuestreo: corta los recordatorios "realízate el examen".
            notificacionService.desactivarRecordatorioNoExamen(cuentaUsuario.publicId)

            val now = LocalDateTime.now()
            val notificacion = NotificacionRequest(
                cuentaUsuarioPublicId = cuentaUsuario.publicId,
                tipoNotificacion = TipoNotificacionEnum.RECORDATORIO_NO_ENTREGA_DISPOSITIVO,
                titulo = NOT_TITULO_NO_ENTREGA,
                mensaje = NOT_MENSAJE_NO_ENTREGA,
                tipoAccion = TipoAccionNotificacionEnum.valueOf(NOT_TIPO_ACCION_NO_ENTREGA),
                accion = NOT_ACCION_NO_ENTREGA
            )

            val programada = NotificacionProgramadaRequest(
                notificacionPublicId = UUID.randomUUID(),
                tipoNotificacion = notificacion.tipoNotificacion,
                titulo = notificacion.titulo,
                mensaje = notificacion.mensaje,
                tipoAccion = notificacion.tipoAccion,
                accion = notificacion.accion,
                proxFecha = now.plusDays(7), // Próxima vez en 7 días
                limiteFecha = now.plusDays(59) // Máximo hasta 2 meses
            )

            notificacionService.crearNotificacionProgramada(
                requestNotificacion = notificacion,
                requestProgramada = programada
            )





        } else {
            throw BadRequestException("La informacion de la prueba es requerida")
        }
        log.info("Sesion de chat creada correctamente")
    }

    override fun getSesionChat(publicId: UUID): List<SesionChatResponse> {
        log.info("Consultando sesiones de chat por usuario - PublicId: $publicId")
        val sesion = sesionChatRepository.findByPacienteCuentaPublicId(publicId)
        log.info("Sesiones de chat consultada correctamene - Total: ${sesion.size}")
        return sesion.map { it.toResponse() }
    }

    override fun getTodosSesionChat(): List<SesionChatResponse> {
        log.info("Consultando todas las sesiones de chat")
        val sesiones = sesionChatRepository.findAll().map { it.toResponse() }
        log.info("Sesiones de chat consultadas - Total: ${sesiones.size}")
        return sesiones
    }

    override fun deleteSesionChat(publicId: UUID) {
        log.info("Eliminando sesion de chat - PublicId: $publicId")
        val sesion = sesionChatRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("La sesion de chat no existe")
        }
        sesionChatRepository.delete(sesion)
        log.info("Sesion de chat eliminada correctamente")
    }

    override fun iniciarSesionChatbot(request: SesionChatbotInicioRequest): UUID {
        log.info("Iniciando sesión chatbot - Usuario: ${request.cuentaPublicId}")
        val paciente = pacienteRepository.findByCuentaPublicId(request.cuentaPublicId).orElseThrow {
            NotFoundException("La cuenta de usuario no existe")
        }
        val sesion = sesionChatRepository.save(
            SesionChatEntity(
                tipo = "CHATBOT",
                inicio = request.inicio,
                fin = request.inicio,
                paciente = paciente
            )
        )
        log.info("Sesión chatbot iniciada - SessionId: ${sesion.publicId}")
        metricaUsoService.registrarInicioSesionChatbot(request.cuentaPublicId)
        return sesion.publicId
    }

    override fun finalizarSesionChatbot(sessionPublicId: UUID, request: SesionChatbotFinRequest) {
        log.info("Finalizando sesión chatbot - SessionId: $sessionPublicId")
        val sesion = sesionChatRepository.findByPublicId(sessionPublicId).orElseThrow {
            NotFoundException("La sesión de chatbot no existe")
        }
        val mensajes = request.mensajesPaciente.coerceAtLeast(0)
        sesion.fin = request.fin
        sesion.huboInteraccion = mensajes >= 1
        sesion.mensajesPaciente = mensajes
        sesionChatRepository.save(sesion)

        val duracionSeg = java.time.Duration.between(sesion.inicio, request.fin).seconds.coerceAtLeast(0)
        metricaUsoService.registrarFinSesionChatbot(sesion.paciente.cuenta.publicId, mensajes, duracionSeg)
        log.info("Sesión chatbot finalizada - Mensajes: $mensajes, Duración: ${duracionSeg}s")
    }

    @Transactional
    override fun crearSesionChatFolleto(request: FolletoRequest) {
        log.info("Registrando sesión folleto - Teléfono: ${request.telefono}")

        rangoTiempoExamenService.validarCodigoActivo(request.saludSexual.ultimoExamenPap)
        rangoTiempoExamenService.validarCodigoActivo(request.saludSexual.tiempoPruebaVph)

        if (cuentaUsuarioRepository.findByNombreUsuario(request.telefono).isPresent) {
            throw BadRequestException("El número de teléfono ya está registrado en el sistema")
        }

        // Cuenta de sistema — la paciente nunca inicia sesión con esta cuenta
        val cuenta = cuentaUsuarioRepository.save(
            CuentaUsuarioEntity(
                nombreUsuario = request.telefono,
                contrasena = passwordEncoder.encode(UUID.randomUUID().toString()),
                rol = "USER",
                aceptaConsentimiento = true
            )
        )

        val fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(request.fechaNacimiento)

        val idiomaEnum = when (request.lenguaMaterna.uppercase().replace("É", "E").replace("Ñ", "N")) {
            "ESPANOL" -> IdiomaEnum.ESPANOL
            "INGLES"  -> IdiomaEnum.INGLES
            else      -> IdiomaEnum.OTRO
        }

        val estadoCivilEnum = EstadoCivilEnum.getEnum(request.estadoCivil)
            ?: EstadoCivilEnum.valueOf(
                request.estadoCivil.uppercase()
                    .replace("/A", "").replace(" ", "_").replace("Ó", "O")
            )

        val paciente = pacienteRepository.save(
            PacienteEntity(
                nombre = "${request.nombre.trim()} ${request.apellido.trim()}",
                fechaNacimiento = fechaNacimiento,
                pais = PaisEnum.valueOf(request.pais.uppercase()),
                zonaResidencial = ZonaResidencialEnum.valueOf(request.zonaResidencial.uppercase()),
                lenguaMaterna = idiomaEnum,
                estadoCivil = estadoCivilEnum,
                sexo = SexoEnum.valueOf(request.sexo.uppercase()),
                cuenta = cuenta
            )
        )

        cuenta.paciente = paciente
        cuentaUsuarioRepository.save(cuenta)

        request.informacionSocioeconomica?.let { infoReq ->
            val ingresoEnum = infoReq.ingresos?.trim()?.takeIf { it.isNotEmpty() }?.let {
                runCatching { IngresoEnum.valueOf(it.uppercase()) }
                    .getOrElse { throw BadRequestException("Ingreso '$it' no válido") }
            }
            val dependencia = runCatching {
                OpcionesSiNoEnum.valueOf(infoReq.dependenciaUniversitaria.trim().uppercase())
            }.getOrElse { OpcionesSiNoEnum.NO }

            val info = informacionSocioeconomicaRepository.save(
                InformacionSocioeconomicaEntity(
                    ingresos = ingresoEnum,
                    dependenciaUniversitaria = dependencia,
                    ocupacion = infoReq.ocupacion?.trim()?.takeIf { it.isNotEmpty() },
                    paciente = paciente
                )
            )
            paciente.informacionSocioeconomica = info
            pacienteRepository.save(paciente)
        }

        val ahora = LocalDateTime.now()
        val sesion = sesionChatRepository.save(
            SesionChatEntity(
                tipo = "EXAMEN",
                origen = "FOLLETO",
                inicio = ahora,
                fin = ahora,
                paciente = paciente
            )
        )

        val saludSexual = saludSexualRepository.save(request.saludSexual.toEntity())

        val fechaExamenDt = LocalDateTime.parse(request.fechaExamen, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val fechaRegistroDt = request.fechaRegistro
            ?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
            ?: LocalDateTime.now()

        dispositivoRegistradoRepository.save(
            DispositivoRegistradoEntity(
                dispositivo = request.dispositivo,
                pacienteId = paciente.id!!,
                paciente = paciente,
                fechaRegistro = fechaRegistroDt
            )
        )

        val examen = examenVphRepository.save(
            ExamenVphEntity(
                fechaExamen = fechaExamenDt,
                dispositivo = request.dispositivo,
                sesionChat = sesion,
                saludSexual = saludSexual
            )
        )

        saludSexual.examenVph = examen
        sesion.examenVph = examen
        sesionChatRepository.save(sesion)
        saludSexualRepository.save(saludSexual)
        paciente.sesionChat.add(sesion)
        pacienteRepository.save(paciente)

        log.info("Sesión folleto registrada - Dispositivo: ${request.dispositivo}, fechaRegistro: $fechaRegistroDt, fechaExamen: $fechaExamenDt")
    }

}