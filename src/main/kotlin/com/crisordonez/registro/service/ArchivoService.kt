package com.crisordonez.registro.service

import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.ArchivoMapper.toEntity
import com.crisordonez.registro.model.mapper.ArchivoMapper.toEntityUpdated
import com.crisordonez.registro.model.mapper.ArchivoMapper.toResponse
import com.crisordonez.registro.model.responses.ArchivoResponse
import com.crisordonez.registro.repository.ArchivoRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ArchivoService(
    @Autowired private val archivoRepository: ArchivoRepository
) : ArchivoServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun crearArchivo(archivo: MultipartFile) {
        log.info("Creando archivo")
        archivoRepository.save(archivo.toEntity())
        log.info("Archivo creado correctamente")
    }

    override fun getArchivo(publicId: UUID): ArchivoResponse {
        log.info("Consultando archivo - PublicId: $publicId")
        val archivo = archivoRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe el archivo solicitado")
        }
        log.info("Archivo consultado correctamente")
        return archivo.toResponse()
    }

    override fun getArchivoByNombre(nombre: String): ArchivoResponse {
        log.info("Consultando archivo - Nombre: $nombre")
        val archivo = archivoRepository.findByNombre(nombre).orElseThrow {
            throw NotFoundException("No existe el archivo solicitado")
        }
        log.info("Archivo consultado por nombre correctamente")
        return archivo.toResponse()
    }

    override fun getTodosArchivos(): List<ArchivoResponse> {
        log.info("Consultando todos los archivos del sistema")
        val archivos = archivoRepository.findAll().map { it.toResponse() }
        log.info("Archivos consultado - Total: ${archivos.size}")
        return archivos
    }

    override fun editarArchivo(publicId: UUID, archivo: MultipartFile) {
        log.info("Editando archivo - PublicId: $publicId")
        val archivoExistente = archivoRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("El archivo no existe")
        }
        archivoRepository.save(archivo.toEntityUpdated(archivoExistente))
        log.info("Archivo editado correctamente")
    }

    override fun eliminarArchivo(publicId: UUID) {
        log.info("Eliminando archivo - PublicId: $publicId")
        val archivo = archivoRepository.findByPublicId(publicId).orElseThrow {
            throw NotFoundException("No existe el archivo requerido")
        }
        archivoRepository.delete(archivo)
        log.info("Archivo eliminado correctamente")
    }
}