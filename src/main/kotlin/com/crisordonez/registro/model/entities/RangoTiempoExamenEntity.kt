package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.util.UUID

/**
 * Catálogo de opciones de tiempo para las preguntas de salud sexual.
 * Cada fila pertenece a UNA pregunta (`pregunta`): MENSTRUACION, PAPANICOLAOU o VPH.
 * Administrado desde clias-admin (activo = true/false por fila).
 */
@Entity
@Table(name = "rango_tiempo_examen")
data class RangoTiempoExamenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    /** Pregunta a la que pertenece: MENSTRUACION | PAPANICOLAOU | VPH. */
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'PAPANICOLAOU'")
    var pregunta: String = "PAPANICOLAOU",

    /** Código estable que se persiste en salud_sexual (ej. PAP_MENOS_1_ANIO). Único global. */
    @Column(nullable = false, unique = true)
    var codigo: String,

    /** Texto mostrado al usuario (ej. "Menos de 1 año"). */
    @Column(nullable = false)
    var etiqueta: String,

    @Column(nullable = false, columnDefinition = "boolean default true")
    var activo: Boolean = true,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var orden: Int = 0,

) : AuditModel()
