package com.crisordonez.registro.model.responses

import java.util.UUID

data class RecursoMultimediaResponse(
    val publicId: UUID,
    val slug: String,
    val tipo: String,
    /** APP | WEB */
    val destino: String,
    val categoria: String?,
    val descripcion: String?,
    val nombreArchivo: String,
    val contentType: String,
    val tamano: Long,
    val activo: Boolean,
    val orden: Int,
    /** Ruta para descargar/servir el binario. */
    val url: String
)
