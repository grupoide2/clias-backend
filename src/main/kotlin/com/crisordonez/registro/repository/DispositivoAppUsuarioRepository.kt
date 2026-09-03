package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.DispositivoAppUsuarioEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface DispositivoAppUsuarioRepository : CrudRepository<DispositivoAppUsuarioEntity, Long> {
    fun findByPublicId(publicId: UUID): Optional<DispositivoAppUsuarioEntity>
    fun findAllByUsuarioPublicId(usuarioPublicId: UUID): List<DispositivoAppUsuarioEntity>
    fun findTopByUsuarioPublicIdOrderByFechaRegistroDesc(usuarioPublicId: UUID): DispositivoAppUsuarioEntity?
    fun findByFcmToken(fcmToken: String): DispositivoAppUsuarioEntity?
    fun deleteByFcmToken(fcmToken: String)

}