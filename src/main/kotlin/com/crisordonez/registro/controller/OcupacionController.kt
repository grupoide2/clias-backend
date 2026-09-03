package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.OcupacionRequest
import com.crisordonez.registro.model.responses.OcupacionResponse
import com.crisordonez.registro.service.OcupacionServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/ocupaciones")
class OcupacionController(
    private val ocupacionService: OcupacionServiceInterface
) {

    /** Público: lo consume la ficha socioeconómica de clias-app antes del registro. */
    @GetMapping
    fun listar(
        @RequestParam(required = false, defaultValue = "true") soloActivas: Boolean,
        @RequestParam(required = false) soloUniversidad: Boolean?
    ): ResponseEntity<List<OcupacionResponse>> =
        ResponseEntity.ok(ocupacionService.listar(soloActivas, soloUniversidad))

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun crear(@Valid @RequestBody request: OcupacionRequest): ResponseEntity<OcupacionResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(ocupacionService.crear(request))

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{publicId}")
    fun actualizar(
        @PathVariable publicId: UUID,
        @Valid @RequestBody request: OcupacionRequest
    ): ResponseEntity<OcupacionResponse> =
        ResponseEntity.ok(ocupacionService.actualizar(publicId, request))

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminar(@PathVariable publicId: UUID) = ocupacionService.eliminar(publicId)
}
