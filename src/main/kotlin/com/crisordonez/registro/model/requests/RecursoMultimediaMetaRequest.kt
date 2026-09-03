package com.crisordonez.registro.model.requests

/** Actualización de metadata de un recurso (sin re-subir el archivo). */
data class RecursoMultimediaMetaRequest(
    val activo: Boolean = true,
    val orden: Int = 0,
    /** APP | WEB — opcional; si viene null no se cambia. */
    val destino: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null
)
