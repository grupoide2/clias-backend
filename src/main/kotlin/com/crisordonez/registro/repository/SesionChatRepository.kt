package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.SesionChatEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface SesionChatRepository: CrudRepository<SesionChatEntity, Long> {

    fun findByPacienteCuentaPublicId(publicId: UUID): List<SesionChatEntity>

    @Query("SELECT COUNT(s) FROM SesionChatEntity s WHERE s.tipo = 'CHATBOT'")
    fun countChatbot(): Long

    fun findByPublicId(publicId: UUID): Optional<SesionChatEntity>

    fun findByExamenVphDispositivo(dispositivo: String): SesionChatEntity?

    fun findTopByPacienteCuentaPublicIdAndTipoOrderByInicioDesc(
        publicId: UUID,
        tipo: String
    ): Optional<SesionChatEntity>

}