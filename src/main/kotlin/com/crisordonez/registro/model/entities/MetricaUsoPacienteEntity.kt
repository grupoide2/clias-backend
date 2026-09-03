package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * Métricas de uso acumuladas por paciente (chatbot + app).
 * Una fila por paciente. Reemplaza a las columnas de métricas de `cuentas_usuario`,
 * que quedan congeladas (ya no se escriben).
 */
@Entity
@Table(name = "metrica_uso_paciente")
data class MetricaUsoPacienteEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @OneToOne
    @JoinColumn(name = "cuenta_usuario_id", nullable = false, unique = true)
    var cuentaUsuario: CuentaUsuarioEntity,

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    var paciente: PacienteEntity,

    /** +1 cada vez que la paciente ingresa al chatbot, interactúe o no. */
    @Column(name = "sesiones_chatbot_totales", nullable = false, columnDefinition = "integer default 0")
    var sesionesChatbotTotales: Int = 0,

    /** +1 por sesión de chatbot con >= 1 mensaje de la paciente, y +1 al completar el formulario de automuestreo. */
    @Column(name = "sesiones_chatbot_exitosas", nullable = false, columnDefinition = "integer default 0")
    var sesionesChatbotExitosas: Int = 0,

    /** Total de mensajes/consultas enviados por la paciente al chatbot. */
    @Column(name = "total_mensajes_paciente", nullable = false, columnDefinition = "integer default 0")
    var totalMensajesPaciente: Int = 0,

    /** Tiempo acumulado de uso del chatbot, en segundos. */
    @Column(name = "tiempo_uso_chat_seg", nullable = false, columnDefinition = "bigint default 0")
    var tiempoUsoChatSeg: Long = 0,

    /** Tiempo acumulado de uso de la app, en segundos. */
    @Column(name = "tiempo_uso_app_seg", nullable = false, columnDefinition = "bigint default 0")
    var tiempoUsoAppSeg: Long = 0,

    /** Primer ingreso a la app (se fija una sola vez). */
    @Column(name = "primer_uso_app")
    var primerUsoApp: LocalDateTime? = null,

    /** Último ingreso/salida de la app. */
    @Column(name = "ultimo_uso_app")
    var ultimoUsoApp: LocalDateTime? = null,

) : AuditModel()
