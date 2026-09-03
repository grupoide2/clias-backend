package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.ArchivoEntity
import com.crisordonez.registro.model.responses.ArchivoResponse
import org.springframework.web.multipart.MultipartFile

object ArchivoMapper {

    fun MultipartFile.toEntity(): ArchivoEntity {
        return ArchivoEntity(
            nombre = this.originalFilename ?: ("unknown" + this.size),
            tipo = this.contentType ?: "unknown",
            contenido = this.bytes,
            tamano = this.size
        )
    }

    fun MultipartFile.toEntityUpdated(archivo: ArchivoEntity): ArchivoEntity {
        archivo.nombre = this.originalFilename ?: ("unknown" + this.size)
        archivo.tipo = this.contentType ?: "unknown"
        archivo.contenido = this.bytes
        archivo.tamano = this.size

        return archivo
    }

    fun ArchivoEntity.toResponse(): ArchivoResponse {
        return ArchivoResponse(
            publicId = this.publicId,
            nombre = this.nombre,
            tipo = this.tipo,
            tamano = this.tamano,
            contenido = this.contenido
        )
    }

}