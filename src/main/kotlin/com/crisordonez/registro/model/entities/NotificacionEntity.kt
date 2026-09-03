package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum

/**
 * Entidad base que almacena todas las notificaciones visibles en el historial,
 * tanto puntuales como disparos recurrentes de plantillas programadas.
 */
@Entity
@Table(name = "notificacion")
data class NotificacionEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val publicId: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuentaUsuario_id", nullable = false)
    val cuentaUsuario: CuentaUsuarioEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipo_notificacion: TipoNotificacionEnum,

    @Column(nullable = false)
    val titulo: String, // Title displayed in the app

    @Column(nullable = false, columnDefinition = "TEXT")
    val mensaje: String,
    /**
     * Define la acción al hacer click en la notificación.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipo_accion: TipoAccionNotificacionEnum,

    /**
     * Datos de la acción: URL de PDF, ID de recurso de video, etc.
     */
    @Column(nullable = true)
    val accion: String? = null,

    @Column(nullable = false)
    val fecha_creacion: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var notificacion_leida: Boolean = false

)