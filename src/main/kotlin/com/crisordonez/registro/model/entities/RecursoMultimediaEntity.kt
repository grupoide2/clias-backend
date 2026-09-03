package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.DestinoRecursoEnum
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import jakarta.persistence.*
import java.util.UUID

/**
 * Recurso multimedia administrado (video de uso de la app, capturas del sitio web…).
 * El binario vive en disco ({app.recursos.dir}/{public_id}); aquí solo va la metadata.
 * Se identifica por `slug` estable (ej. "video_uso_app", "captura_web_1").
 *
 * `destino` + `categoria` + `descripcion` dicen para quién es el recurso y en qué
 * pantalla se usa, para que el admin sepa qué está reemplazando.
 */
@Entity
@Table(name = "recurso_multimedia")
data class RecursoMultimediaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var slug: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoArchivoEnum,

    /** Para quién es: APP (clias-app) o WEB (clias-web). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) default 'APP'")
    var destino: DestinoRecursoEnum = DestinoRecursoEnum.APP,

    /** Agrupador libre (ej. VIDEO_EDUCATIVO, PORTADA_BLOG, CAPTURA_APP). */
    @Column(length = 40)
    var categoria: String? = null,

    /** Texto para el admin: qué es y en qué pantalla aparece. */
    @Column(columnDefinition = "text")
    var descripcion: String? = null,

    @Column(name = "nombre_archivo", nullable = false)
    var nombreArchivo: String,

    @Column(name = "content_type", nullable = false)
    var contentType: String,

    @Column(nullable = false)
    var tamano: Long = 0,

    @Column(nullable = false, columnDefinition = "boolean default true")
    var activo: Boolean = true,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var orden: Int = 0,

) : AuditModel()
