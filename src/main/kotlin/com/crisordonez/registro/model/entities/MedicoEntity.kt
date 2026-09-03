package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.SexoEnum

import com.fasterxml.jackson.annotation.JsonIgnore

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "medicos")
data class MedicoEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var nombre: String,

    @Column(nullable = false, unique = true)
    var usuario: String = "",

    @JsonIgnore
    @Column(nullable = false)
    var contrasena: String = "",

    @Enumerated(EnumType.STRING)
    var sexo: SexoEnum = SexoEnum.FEMENINO,

    @Column(nullable = false)
    var correo: String,

    var especializacion: String? = null,

    @Column(name = "n_registro")
    var nRegistro: String? = null,

    @OneToMany(fetch = FetchType.LAZY)
    var evoluciones: MutableList<EvolucionEntity> = mutableListOf(),

    @Column(name = "rol", nullable = false)
    var rol: String = "MEDICO"

) : AuditModel()
