package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.FolletoRequest
import com.crisordonez.registro.model.requests.SesionChatRequest
import com.crisordonez.registro.model.requests.SesionChatbotInicioRequest
import com.crisordonez.registro.model.requests.SesionChatbotFinRequest
import com.crisordonez.registro.model.responses.SesionChatResponse
import com.crisordonez.registro.service.SesionChatServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/sesion-chat")
class SesionChatController(
    private val sesionChatServiceInterface: SesionChatServiceInterface
) {

    // ── Usuario autenticado (rol USER) ────────────────────────────────────────

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/usuario")
    fun crearSesionChat(@Valid @RequestBody sesion: SesionChatRequest): ResponseEntity<Unit> =
        ResponseEntity.ok(sesionChatServiceInterface.crearSesionChat(sesion))

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/usuario/{publicId}")
    fun getSesionChat(@PathVariable publicId: UUID): ResponseEntity<List<SesionChatResponse>> =
        ResponseEntity.ok(sesionChatServiceInterface.getSesionChat(publicId))

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/chatbot/inicio")
    fun iniciarSesionChatbot(@RequestBody request: SesionChatbotInicioRequest): ResponseEntity<UUID> =
        ResponseEntity.ok(sesionChatServiceInterface.iniciarSesionChatbot(request))

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/chatbot/{sessionPublicId}/fin")
    fun finalizarSesionChatbot(
        @PathVariable sessionPublicId: UUID,
        @RequestBody request: SesionChatbotFinRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(sesionChatServiceInterface.finalizarSesionChatbot(sessionPublicId, request))

    // ── Administrador (rol ADMIN) ──────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodosSesionChat(): ResponseEntity<List<SesionChatResponse>> =
        ResponseEntity.ok(sesionChatServiceInterface.getTodosSesionChat())

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{publicId}")
    fun eliminarSesionChat(@PathVariable publicId: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(sesionChatServiceInterface.deleteSesionChat(publicId))

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/folleto")
    fun crearSesionChatFolleto(@Valid @RequestBody request: FolletoRequest): ResponseEntity<Unit> {
        sesionChatServiceInterface.crearSesionChatFolleto(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
