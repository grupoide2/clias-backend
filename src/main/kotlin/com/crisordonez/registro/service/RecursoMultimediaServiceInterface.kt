package com.crisordonez.registro.service

import com.crisordonez.registro.model.enums.DestinoRecursoEnum
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import com.crisordonez.registro.model.requests.RecursoMultimediaMetaRequest
import com.crisordonez.registro.model.responses.RecursoMultimediaResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/** Binario listo para servir (no expuesto como JSON). */
class RecursoContenido(
    val bytes: ByteArray,
    val contentType: String,
    val nombreArchivo: String
)

interface RecursoMultimediaServiceInterface {

    fun listar(
        tipo: TipoArchivoEnum?,
        destino: DestinoRecursoEnum?,
        soloActivos: Boolean
    ): List<RecursoMultimediaResponse>

    /** Crea o reemplaza (upsert por slug) el recurso y su binario en disco. */
    fun subir(
        slug: String,
        tipo: TipoArchivoEnum,
        destino: DestinoRecursoEnum,
        categoria: String?,
        descripcion: String?,
        archivo: MultipartFile
    ): RecursoMultimediaResponse

    fun actualizarMeta(publicId: UUID, meta: RecursoMultimediaMetaRequest): RecursoMultimediaResponse

    fun eliminar(publicId: UUID)

    /** Contenido del recurso activo identificado por slug. */
    fun contenido(slug: String): RecursoContenido
}
