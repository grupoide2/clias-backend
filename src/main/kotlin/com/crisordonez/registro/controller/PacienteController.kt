package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.DispositivoRegistradoRequest
import com.crisordonez.registro.model.requests.PacienteRequest
import com.crisordonez.registro.model.responses.EstadoAutomuestreoResponse
import com.crisordonez.registro.model.responses.PacienteResponse
import com.crisordonez.registro.service.PacienteServiceInterface
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/paciente")
class PacienteController(
    private val pacienteServiceInterface: PacienteServiceInterface
) {

    // ── Usuario autenticado (rol USER) ────────────────────────────────────────

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/editar/{publicId}")
    fun editarPaciente(
        @PathVariable publicId: UUID,
        @Valid @RequestBody paciente: PacienteRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(pacienteServiceInterface.editarPaciente(publicId, paciente))

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/usuario/{publicId}")
    fun getPaciente(@PathVariable publicId: UUID): ResponseEntity<PacienteResponse> =
        ResponseEntity.ok(pacienteServiceInterface.getPaciente(publicId))

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/estado-automuestreo/{publicId}")
    fun estadoAutomuestreo(@PathVariable publicId: UUID): ResponseEntity<EstadoAutomuestreoResponse> =
        ResponseEntity.ok(pacienteServiceInterface.estadoAutomuestreo(publicId))

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/registrar-dispositivo/{publicId}")
    fun registrarDispositivo(
        @PathVariable publicId: UUID,
        @Valid @RequestBody dispositivo: DispositivoRegistradoRequest
    ): ResponseEntity<String> =
        ResponseEntity.ok(pacienteServiceInterface.registrarDispositivo(publicId, dispositivo))

    // ── Administrador (rol ADMIN) ──────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodosPacientes(): ResponseEntity<List<PacienteResponse>> =
        ResponseEntity.ok(pacienteServiceInterface.getTodosPacientes())

    // ── Público — usado por admin web para buscar paciente por código de dispositivo

    @GetMapping("/dispositivo/{codigo}")
    fun getByDispositivo(@PathVariable codigo: String): ResponseEntity<PacienteResponse> {
        val dto = pacienteServiceInterface.findByDispositivo(codigo)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dto)
    }
}
