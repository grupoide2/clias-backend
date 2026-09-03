package com.crisordonez.registro.service

import com.crisordonez.registro.model.responses.ArchivoResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface ArchivoServiceInterface {

    fun crearArchivo(archivo: MultipartFile)

    fun getArchivo(publicId: UUID): ArchivoResponse

    fun getArchivoByNombre(nombre: String): ArchivoResponse

    fun getTodosArchivos(): List<ArchivoResponse>

    fun editarArchivo(publicId: UUID, archivo: MultipartFile)

    fun eliminarArchivo(publicId: UUID)

}