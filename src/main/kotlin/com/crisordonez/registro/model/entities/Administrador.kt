package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(
    name = "administrador",
    uniqueConstraints = [UniqueConstraint(columnNames = ["usuario"])]
)
data class Administrador(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "public_id", nullable = false, updatable = false)
    val publicId: UUID = UUID.randomUUID(),

    @Column(name = "nombre", nullable = false)
    var nombre: String,

    @Column(name = "usuario", nullable = false, unique = true)
    var usuario: String,

    @Column(name = "contrasena", nullable = false)
    var contrasenaHash: String,

    @Column(name = "last_login")
    var lastLogin: OffsetDateTime? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "rol", nullable = false)
    var rol: String = "ADMIN"
)
