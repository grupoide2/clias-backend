package com.crisordonez.registro.controller

import com.crisordonez.registro.model.responses.ArchivoResponse
import com.crisordonez.registro.service.ArchivoServiceInterface
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/archivo")
class ArchivoController(
    private val archivoServiceInterface: ArchivoServiceInterface
) {

    // ── Público — app móvil descarga archivos por nombre ──────────────────────

    @GetMapping("/nombre/{nombre}")
    fun getArchivoNombre(@PathVariable nombre: String): ResponseEntity<ArchivoResponse> =
        ResponseEntity.ok(archivoServiceInterface.getArchivoByNombre(nombre))

    // ── Administrador (rol ADMIN) ──────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    fun crearArchivo(@RequestParam("archivo") archivo: MultipartFile): ResponseEntity<Unit> =
        ResponseEntity.ok(archivoServiceInterface.crearArchivo(archivo))

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{publicId}")
    fun getArchivo(@PathVariable publicId: UUID): ResponseEntity<ArchivoResponse> =
        ResponseEntity.ok(archivoServiceInterface.getArchivo(publicId))

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodosArchivos(): ResponseEntity<List<ArchivoResponse>> =
        ResponseEntity.ok(archivoServiceInterface.getTodosArchivos())

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/editar/{publicId}")
    fun editarArchivo(
        @PathVariable publicId: UUID,
        @RequestParam("archivo") archivo: MultipartFile
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(archivoServiceInterface.editarArchivo(publicId, archivo))

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/eliminar/{publicId}")
    fun eliminarArchivo(@PathVariable publicId: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(archivoServiceInterface.eliminarArchivo(publicId))
}
