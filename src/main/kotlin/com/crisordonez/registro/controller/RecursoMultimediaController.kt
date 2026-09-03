package com.crisordonez.registro.controller

import com.crisordonez.registro.model.enums.DestinoRecursoEnum
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import com.crisordonez.registro.model.requests.RecursoMultimediaMetaRequest
import com.crisordonez.registro.model.responses.RecursoMultimediaResponse
import com.crisordonez.registro.service.RecursoMultimediaServiceInterface
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/recursos")
class RecursoMultimediaController(
    private val service: RecursoMultimediaServiceInterface
) {

    // ── Público ──────────────────────────────────────────────────────────────

    /** Lista de recursos (metadata). `?tipo=IMAGEN&destino=APP&soloActivos=true` */
    @GetMapping
    fun listar(
        @RequestParam(required = false) tipo: TipoArchivoEnum?,
        @RequestParam(required = false) destino: DestinoRecursoEnum?,
        @RequestParam(required = false, defaultValue = "true") soloActivos: Boolean
    ): ResponseEntity<List<RecursoMultimediaResponse>> =
        ResponseEntity.ok(service.listar(tipo, destino, soloActivos))

    /** Sirve el binario del recurso identificado por slug. */
    @GetMapping("/{slug}")
    fun servir(@PathVariable slug: String): ResponseEntity<ByteArray> {
        val c = service.contenido(slug)
        return ResponseEntity.ok()
            .contentType(runCatching { MediaType.parseMediaType(c.contentType) }
                .getOrDefault(MediaType.APPLICATION_OCTET_STREAM))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${c.nombreArchivo}\"")
            .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
            .body(c.bytes)
    }

    // ── Administrador (rol ADMIN) ────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun subir(
        @RequestParam slug: String,
        @RequestParam tipo: TipoArchivoEnum,
        @RequestParam(required = false, defaultValue = "APP") destino: DestinoRecursoEnum,
        @RequestParam(required = false) categoria: String?,
        @RequestParam(required = false) descripcion: String?,
        @RequestParam("archivo") archivo: MultipartFile
    ): ResponseEntity<RecursoMultimediaResponse> =
        ResponseEntity.ok(service.subir(slug, tipo, destino, categoria, descripcion, archivo))

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{publicId}")
    fun actualizarMeta(
        @PathVariable publicId: UUID,
        @RequestBody meta: RecursoMultimediaMetaRequest
    ): ResponseEntity<RecursoMultimediaResponse> =
        ResponseEntity.ok(service.actualizarMeta(publicId, meta))

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{publicId}")
    fun eliminar(@PathVariable publicId: UUID): ResponseEntity<Unit> {
        service.eliminar(publicId)
        return ResponseEntity.noContent().build()
    }
}
