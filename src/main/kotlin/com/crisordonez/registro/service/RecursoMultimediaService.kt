package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.RecursoMultimediaEntity
import com.crisordonez.registro.model.enums.DestinoRecursoEnum
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import com.crisordonez.registro.model.errors.BadRequestException
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.requests.RecursoMultimediaMetaRequest
import com.crisordonez.registro.model.responses.RecursoMultimediaResponse
import com.crisordonez.registro.repository.RecursoMultimediaRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class RecursoMultimediaService(
    private val repo: RecursoMultimediaRepository,
    @Value("\${app.recursos.dir:storage/recursos}") private val dirConfig: String,
) : RecursoMultimediaServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private lateinit var dir: Path

    @PostConstruct
    fun init() {
        dir = Paths.get(dirConfig).toAbsolutePath().normalize()
        Files.createDirectories(dir)
        log.info("Directorio de recursos multimedia: $dir")
    }

    private fun RecursoMultimediaEntity.toResponse() = RecursoMultimediaResponse(
        publicId = publicId,
        slug = slug,
        tipo = tipo.name,
        destino = destino.name,
        categoria = categoria,
        descripcion = descripcion,
        nombreArchivo = nombreArchivo,
        contentType = contentType,
        tamano = tamano,
        activo = activo,
        orden = orden,
        url = "/recursos/$slug"
    )

    private fun normalizarSlug(slug: String): String =
        slug.trim().lowercase().replace(Regex("[^a-z0-9_-]"), "_").trim('_')

    override fun listar(
        tipo: TipoArchivoEnum?,
        destino: DestinoRecursoEnum?,
        soloActivos: Boolean
    ): List<RecursoMultimediaResponse> {
        val base = when {
            tipo != null && soloActivos -> repo.findAllByTipoAndActivoTrueOrderByOrdenAscSlugAsc(tipo)
            tipo != null -> repo.findAllByTipoOrderByOrdenAscSlugAsc(tipo)
            soloActivos -> repo.findAllByActivoTrueOrderByOrdenAscSlugAsc()
            else -> repo.findAllByOrderByOrdenAscSlugAsc()
        }
        return base
            .filter { destino == null || it.destino == destino }
            .map { it.toResponse() }
    }

    @Transactional
    override fun subir(
        slug: String,
        tipo: TipoArchivoEnum,
        destino: DestinoRecursoEnum,
        categoria: String?,
        descripcion: String?,
        archivo: MultipartFile
    ): RecursoMultimediaResponse {
        if (archivo.isEmpty) throw BadRequestException("El archivo está vacío")
        val slugLimpio = normalizarSlug(slug)
        if (slugLimpio.isEmpty()) throw BadRequestException("El slug no es válido")

        val entidad = repo.findBySlug(slugLimpio).orElseGet {
            RecursoMultimediaEntity(
                slug = slugLimpio,
                tipo = tipo,
                nombreArchivo = archivo.originalFilename ?: slugLimpio,
                contentType = archivo.contentType ?: "application/octet-stream"
            )
        }
        entidad.tipo = tipo
        entidad.destino = destino
        categoria?.trim()?.takeIf { it.isNotEmpty() }?.let { entidad.categoria = it }
        descripcion?.trim()?.takeIf { it.isNotEmpty() }?.let { entidad.descripcion = it }
        entidad.nombreArchivo = archivo.originalFilename ?: slugLimpio
        entidad.contentType = archivo.contentType ?: "application/octet-stream"
        entidad.tamano = archivo.size
        val guardada = repo.save(entidad)

        val destino = dir.resolve(guardada.publicId.toString())
        archivo.inputStream.use { input ->
            Files.copy(input, destino, StandardCopyOption.REPLACE_EXISTING)
        }
        log.info("Recurso '${guardada.slug}' subido (${guardada.tamano} bytes) -> $destino")
        return guardada.toResponse()
    }

    @Transactional
    override fun actualizarMeta(publicId: UUID, meta: RecursoMultimediaMetaRequest): RecursoMultimediaResponse {
        val entidad = repo.findByPublicId(publicId)
            .orElseThrow { NotFoundException("El recurso no existe") }
        entidad.activo = meta.activo
        entidad.orden = meta.orden
        meta.destino?.trim()?.takeIf { it.isNotEmpty() }?.let {
            entidad.destino = runCatching { DestinoRecursoEnum.valueOf(it.uppercase()) }
                .getOrElse { throw BadRequestException("destino inválido: $it") }
        }
        meta.categoria?.let { entidad.categoria = it.trim().ifEmpty { null } }
        meta.descripcion?.let { entidad.descripcion = it.trim().ifEmpty { null } }
        return repo.save(entidad).toResponse()
    }

    @Transactional
    override fun eliminar(publicId: UUID) {
        val entidad = repo.findByPublicId(publicId)
            .orElseThrow { NotFoundException("El recurso no existe") }
        runCatching { Files.deleteIfExists(dir.resolve(entidad.publicId.toString())) }
            .onFailure { log.warn("No se pudo borrar el archivo de ${entidad.slug}: ${it.message}") }
        repo.delete(entidad)
        log.info("Recurso '${entidad.slug}' eliminado")
    }

    override fun contenido(slug: String): RecursoContenido {
        val entidad = repo.findBySlug(normalizarSlug(slug))
            .filter { it.activo }
            .orElseThrow { NotFoundException("Recurso no encontrado") }
        val path = dir.resolve(entidad.publicId.toString())
        if (!Files.exists(path)) throw NotFoundException("El archivo del recurso no está disponible")
        return RecursoContenido(Files.readAllBytes(path), entidad.contentType, entidad.nombreArchivo)
    }
}
