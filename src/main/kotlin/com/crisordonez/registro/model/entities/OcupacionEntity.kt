package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.util.UUID

/**
 * Catálogo de ocupaciones para la ficha socioeconómica.
 * Administrado desde clias-admin. `solo_universidad` marca las que aplican
 * cuando la paciente pertenece a la Universidad de Cuenca.
 */
@Entity
@Table(name = "ocupacion")
data class OcupacionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var nombre: String,

    @Column(nullable = false, columnDefinition = "boolean default true")
    var activo: Boolean = true,

    @Column(name = "solo_universidad", nullable = false, columnDefinition = "boolean default false")
    var soloUniversidad: Boolean = false,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var orden: Int = 0,

) : AuditModel()
