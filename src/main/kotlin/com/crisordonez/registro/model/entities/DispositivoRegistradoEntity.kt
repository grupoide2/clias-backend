// src/main/kotlin/com/crisordonez/registro/model/entities/DispositivoRegistradoEntity.kt
package com.crisordonez.registro.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import java.util.UUID


@Entity
@Table(name = "dispositivos_registrados")
data class DispositivoRegistradoEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val dispositivo: String,

    @Column(name = "paciente_id", nullable = false)
    val pacienteId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", insertable = false, updatable = false)
    val paciente: PacienteEntity,

    @Column(nullable = false, unique = true)
    val publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val fechaRegistro: LocalDateTime = LocalDateTime.now(),


)


