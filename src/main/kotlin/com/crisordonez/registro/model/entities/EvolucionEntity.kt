package com.crisordonez.registro.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jdk.jfr.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "evolucion")
data class EvolucionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var temperatura: Double,

    @Column(nullable = false)
    var pulso: Int,

    @Column(nullable = false)
    var talla: Double,

    @Column(nullable = false)
    var peso: Double,

    @Timestamp
    var fecha: LocalDateTime,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "examen_vph_id")
    var examenVph: ExamenVphEntity,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "medico_id")
    var medico: MedicoEntity? = null,


) : AuditModel()
