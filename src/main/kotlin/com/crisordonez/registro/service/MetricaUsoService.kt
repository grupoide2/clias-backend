package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.MetricaUsoPacienteEntity
import com.crisordonez.registro.model.responses.MetricaDashboardResponse
import com.crisordonez.registro.repository.CuentaUsuarioRepository
import com.crisordonez.registro.repository.ExamenVphRepository
import com.crisordonez.registro.repository.MetricaUsoPacienteRepository
import com.crisordonez.registro.repository.PacienteRepository
import com.crisordonez.registro.repository.SesionChatRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class MetricaUsoService(
    private val metricaRepository: MetricaUsoPacienteRepository,
    private val cuentaUsuarioRepository: CuentaUsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val examenVphRepository: ExamenVphRepository,
    private val sesionChatRepository: SesionChatRepository,
) : MetricaUsoServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private fun formatoHms(seg: Long): String {
        val s = seg.coerceAtLeast(0)
        return "%02d:%02d:%02d".format(s / 3600, (s % 3600) / 60, s % 60)
    }

    override fun dashboard(): MetricaDashboardResponse {
        val tiempoAppSeg = (metricaRepository.promedioTiempoUsoAppSeg() ?: 0.0).toLong()
        val tiempoChatSeg = (metricaRepository.promedioTiempoUsoChatSeg() ?: 0.0).toLong()
        return MetricaDashboardResponse(
            pacientesRegistradas = pacienteRepository.count(),
            automuestreosRealizados = examenVphRepository.count(),
            pacientesUsaronChatbot = metricaRepository.countPacientesConChatbot(),
            sesionesChatbot = sesionChatRepository.countChatbot(),
            mensajesEnviadosChatbot = metricaRepository.sumMensajesPaciente(),
            tiempoPromedioAppSeg = tiempoAppSeg,
            tiempoPromedioAppFormato = formatoHms(tiempoAppSeg),
            tiempoPromedioChatbotSeg = tiempoChatSeg,
            tiempoPromedioChatbotFormato = formatoHms(tiempoChatSeg),
            resultadosDisponibles = examenVphRepository.countConResultado(),
        )
    }

    /** Obtiene la fila de métricas de la paciente; la crea si no existe. Devuelve null si la cuenta no tiene paciente. */
    private fun obtenerOCrear(cuentaPublicId: UUID): MetricaUsoPacienteEntity? {
        val existente = metricaRepository.findByCuentaUsuarioPublicId(cuentaPublicId).orElse(null)
        if (existente != null) return existente

        val cuenta = cuentaUsuarioRepository.findByPublicId(cuentaPublicId).orElse(null)
        if (cuenta == null) {
            log.warn("Métrica omitida: la cuenta $cuentaPublicId no existe")
            return null
        }
        val paciente = cuenta.paciente
        if (paciente == null) {
            log.warn("Métrica omitida: la cuenta $cuentaPublicId no tiene paciente asociado")
            return null
        }
        return metricaRepository.save(
            MetricaUsoPacienteEntity(cuentaUsuario = cuenta, paciente = paciente)
        )
    }

    @Transactional
    override fun registrarInicioSesionChatbot(cuentaPublicId: UUID) {
        val m = obtenerOCrear(cuentaPublicId) ?: return
        m.sesionesChatbotTotales += 1
        metricaRepository.save(m)
        log.info("Métrica: inicio de sesión chatbot - cuenta $cuentaPublicId, totales=${m.sesionesChatbotTotales}")
    }

    @Transactional
    override fun registrarFinSesionChatbot(cuentaPublicId: UUID, mensajesPaciente: Int, duracionSeg: Long) {
        val m = obtenerOCrear(cuentaPublicId) ?: return
        val mensajes = mensajesPaciente.coerceAtLeast(0)
        m.totalMensajesPaciente += mensajes
        m.tiempoUsoChatSeg += duracionSeg.coerceAtLeast(0)
        if (mensajes >= 1) m.sesionesChatbotExitosas += 1
        metricaRepository.save(m)
        log.info("Métrica: fin de sesión chatbot - cuenta $cuentaPublicId, mensajes=$mensajes, exitosas=${m.sesionesChatbotExitosas}")
    }

    @Transactional
    override fun registrarAutomuestreoCompletado(cuentaPublicId: UUID) {
        val m = obtenerOCrear(cuentaPublicId) ?: return
        m.sesionesChatbotExitosas += 1
        metricaRepository.save(m)
        log.info("Métrica: automuestreo completado - cuenta $cuentaPublicId, exitosas=${m.sesionesChatbotExitosas}")
    }

    @Transactional
    override fun acumularTiempoChat(cuentaPublicId: UUID, tiempoMinutos: Double) {
        val m = obtenerOCrear(cuentaPublicId) ?: return
        if (tiempoMinutos > 1.0) {
            m.sesionesChatbotExitosas += 1
            m.tiempoUsoChatSeg += (tiempoMinutos * 60).toLong().coerceAtLeast(0)
            metricaRepository.save(m)
        }
        log.info("Métrica: tiempo de chat (legado) - cuenta $cuentaPublicId, +${tiempoMinutos}min")
    }

    @Transactional
    override fun acumularTiempoApp(
        cuentaPublicId: UUID,
        inicio: LocalDateTime,
        fin: LocalDateTime,
        tiempoMinutos: Double
    ) {
        val m = obtenerOCrear(cuentaPublicId) ?: return
        m.tiempoUsoAppSeg += (tiempoMinutos * 60).toLong().coerceAtLeast(0)
        if (m.primerUsoApp == null) m.primerUsoApp = inicio
        m.ultimoUsoApp = fin
        metricaRepository.save(m)
        log.info("Métrica: tiempo de app - cuenta $cuentaPublicId, +${tiempoMinutos}min, acumulado=${m.tiempoUsoAppSeg}s")
    }

    override fun promedioTiempoChatMinutos(): Double {
        val avgSeg = metricaRepository.promedioTiempoUsoChatSeg() ?: return 0.0
        return avgSeg / 60.0
    }
}
