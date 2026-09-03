package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.TipoAccionNotificacionEnum
import com.crisordonez.registro.model.enums.TipoNotificacionEnum
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * Extensión que almacena la lógica de re-ejecución de plantillas de notificación.
 * Cada registro apunta a una NotificacionEntity de tipo PROGRAMADA.
 */

@Entity
@Table(name = "notificacion_programada")
data class NotificacionProgramadaEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0, // ID autoincremental

    @Column(nullable = false, unique = true)
    val publicId: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_usuario_id", nullable = false)
    val cuentaUsuario: CuentaUsuarioEntity,

    @Column(nullable = false)
    val titulo: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val mensaje: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoNotificacion: TipoNotificacionEnum,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoAccion: TipoAccionNotificacionEnum,

    @Column(nullable = true)
    val accion: String? = null,

    @Column(nullable = false)
    var programacionActiva: Boolean = true,

    @Column(nullable = false)
    val fechaInicio: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var proxFecha: LocalDateTime,

    @Column(nullable = true)
    val limiteFecha: LocalDateTime? = null


)
