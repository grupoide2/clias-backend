package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.InformacionSocioeconomicaRequest
import com.crisordonez.registro.model.responses.InformacionSocioeconomicaResponse
import com.crisordonez.registro.service.InformacionSocioeconomicaServiceInterface
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/info-socioeconomica")
class InformacionSocioeconomicaController(
    private val informacionSocioeconomicaServiceInterface: InformacionSocioeconomicaServiceInterface
) {

    // ── Público — app móvil verifica la ficha antes de completar registro ─────

    @GetMapping("/ficha/existe/{cuentaUsuarioPublicId}")
    fun verificarFicha(@PathVariable cuentaUsuarioPublicId: UUID): Map<String, Boolean> =
        mapOf("existeFicha" to informacionSocioeconomicaServiceInterface.existeFichaSocioeconomica(cuentaUsuarioPublicId))

    // ── Usuario autenticado (rol USER) ────────────────────────────────────────

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/editar/{publicId}")
    fun editarInformaion(
        @PathVariable publicId: UUID,
        @Valid @RequestBody informacion: InformacionSocioeconomicaRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(informacionSocioeconomicaServiceInterface.editarInfoSocioeconomica(publicId, informacion))

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/usuario/{publicId}")
    fun getInformacion(@PathVariable publicId: UUID): ResponseEntity<InformacionSocioeconomicaResponse?> =
        ResponseEntity.ok(informacionSocioeconomicaServiceInterface.getInfoSocioeconomica(publicId))

    // ── Administrador (rol ADMIN) ──────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodosInformacion(): ResponseEntity<List<InformacionSocioeconomicaResponse>> =
        ResponseEntity.ok(informacionSocioeconomicaServiceInterface.getTodosInfo())
}
