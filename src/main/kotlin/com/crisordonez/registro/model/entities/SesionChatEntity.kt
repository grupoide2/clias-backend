package com.crisordonez.registro.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jdk.jfr.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "sesion_chat")
data class SesionChatEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 15, columnDefinition = "varchar(15) default 'EXAMEN'")
    var tipo: String = "EXAMEN", // EXAMEN | CHATBOT

    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) default 'APP'")
    var origen: String = "APP", // APP | FOLLETO

    @Timestamp
    var inicio: LocalDateTime,

    @Timestamp
    var fin: LocalDateTime,

    @Column(columnDefinition = "TEXT")
    var contenido: String? = null,

    // Indicadores de interacción con el chatbot (aplican a tipo = CHATBOT).
    // No se almacena el contenido de las conversaciones.
    @Column(name = "hubo_interaccion", nullable = false, columnDefinition = "boolean default false")
    var huboInteraccion: Boolean = false,

    @Column(name = "mensajes_paciente", nullable = false, columnDefinition = "integer default 0")
    var mensajesPaciente: Int = 0,

    @OneToOne
    @JoinColumn(name = "examen_vph_id")
    var examenVph: ExamenVphEntity? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    var paciente: PacienteEntity

) : AuditModel()
