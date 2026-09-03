package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.EvolucionRequest
import com.crisordonez.registro.model.responses.EvolucionResponse
import com.crisordonez.registro.service.EvolucionServiceInterface
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/evolucion")
@PreAuthorize("hasRole('ADMIN')")
class EvolucionController(
    private val evolucionServiceInterface: EvolucionServiceInterface
) {

    @PostMapping("/admin/{publicId}")
    fun crearEvolucion(
        @PathVariable publicId: String,
        @Valid @RequestBody evolucion: EvolucionRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(evolucionServiceInterface.crearEvolucion(publicId, evolucion))

    @GetMapping("/admin/{publicId}")
    fun getEvolucion(@PathVariable publicId: UUID): ResponseEntity<EvolucionResponse> =
        ResponseEntity.ok(evolucionServiceInterface.getEvolucion(publicId))

    @GetMapping("/admin")
    fun getTodasEvoluciones(): ResponseEntity<List<EvolucionResponse>> =
        ResponseEntity.ok(evolucionServiceInterface.getTodasEvoluciones())

    @DeleteMapping("/admin/{publicId}")
    fun eliminarEvolucion(@PathVariable publicId: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(evolucionServiceInterface.eliminarEvolucion(publicId))
}
