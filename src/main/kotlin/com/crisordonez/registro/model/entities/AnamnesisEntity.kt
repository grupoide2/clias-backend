package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "anamnesis")
data class AnamnesisEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var publicId: UUID = UUID.randomUUID(),

    var edadPrimerRelacionSexual: Int? = null,

    var edadPrimerPap: Int? = null,

    @OneToOne
    @JoinColumn(name = "paciente_id")
    var paciente: PacienteEntity

) : AuditModel()
