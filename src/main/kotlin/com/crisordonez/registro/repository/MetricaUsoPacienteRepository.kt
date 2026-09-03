package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.MetricaUsoPacienteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface MetricaUsoPacienteRepository : JpaRepository<MetricaUsoPacienteEntity, Long> {

    fun findByCuentaUsuarioPublicId(publicId: UUID): Optional<MetricaUsoPacienteEntity>

    @Query("SELECT AVG(m.tiempoUsoChatSeg) FROM MetricaUsoPacienteEntity m WHERE m.tiempoUsoChatSeg > 0")
    fun promedioTiempoUsoChatSeg(): Double?

    @Query("SELECT AVG(m.tiempoUsoAppSeg) FROM MetricaUsoPacienteEntity m WHERE m.tiempoUsoAppSeg > 0")
    fun promedioTiempoUsoAppSeg(): Double?

    @Query("SELECT COALESCE(SUM(m.sesionesChatbotTotales), 0) FROM MetricaUsoPacienteEntity m")
    fun sumSesionesChatbot(): Long

    @Query("SELECT COALESCE(SUM(m.totalMensajesPaciente), 0) FROM MetricaUsoPacienteEntity m")
    fun sumMensajesPaciente(): Long

    @Query("SELECT COUNT(m) FROM MetricaUsoPacienteEntity m WHERE m.sesionesChatbotTotales > 0")
    fun countPacientesConChatbot(): Long
}
