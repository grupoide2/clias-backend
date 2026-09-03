package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "codigos_qr")
data class CodigoQREntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val codigo: String,

    @Column(name = "fecha_expiracion", nullable = false)
    val fechaExpiracion: LocalDate,

    @Column(name = "creado_en", nullable = false)
    val creadoEn: LocalDateTime = LocalDateTime.now(),

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    val publicId: UUID = UUID.randomUUID()
)
