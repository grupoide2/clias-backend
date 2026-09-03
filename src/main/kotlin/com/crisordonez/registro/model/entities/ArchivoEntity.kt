package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "archivos")
data class ArchivoEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var tipo: String,

    @Column(nullable = false, columnDefinition = "bytea")
    var contenido: ByteArray,

    @Column(nullable = false)
    var tamano: Long,

    @Column(nullable = false, unique = true)
    var nombre: String

): AuditModel()
