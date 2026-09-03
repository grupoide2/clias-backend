package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dispositivo_app_usuario")
data class DispositivoAppUsuarioEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val publicId: UUID = UUID.randomUUID(),

    @Column(name = "usuario_public_id", nullable = false)
    val usuarioPublicId: UUID,  // Relación por UUID

    @Column(nullable = false)
    val fcmToken: String,

    @Column(nullable = false)
    val fechaRegistro: LocalDateTime = LocalDateTime.now()
)