package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.TipoArchivoEnum
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ArchivoRequest(

    @field:NotNull(message = "El tipo del archivo es requerido")
    @field:NotBlank(message = "El tipo del archivo es requerido")
    val tipo: TipoArchivoEnum,

    @field:NotNull(message = "El contenido del archivo es requerido")
    @field:NotBlank(message = "El contenido del archivo es requerido")
    val contenido: ByteArray,//opcional se puede cambiar por una referencia a otro servidor de archivos

    @field:NotNull(message = "El nombre del archivo es requerido")
    @field:NotBlank(message = "El nombre del archivo es requerido")
    val nombre: String

)
