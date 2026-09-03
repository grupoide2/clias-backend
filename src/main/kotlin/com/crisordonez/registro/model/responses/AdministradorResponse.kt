package com.crisordonez.registro.model.responses

import com.crisordonez.registro.model.entities.Administrador
import java.time.OffsetDateTime
import java.util.UUID

data class AdministradorResponse(
    val publicId: UUID,
    val nombre: String,
    val usuario: String,
    val rol: String,
    val lastLogin: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    companion object {
        fun from(a: Administrador) = AdministradorResponse(
            publicId  = a.publicId,
            nombre    = a.nombre,
            usuario   = a.usuario,
            rol       = a.rol,
            lastLogin = a.lastLogin,
            createdAt = a.createdAt,
            updatedAt = a.updatedAt
        )
    }
}
