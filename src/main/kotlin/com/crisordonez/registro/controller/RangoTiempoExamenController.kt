package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.RangoTiempoExamenRequest
import com.crisordonez.registro.model.responses.RangoTiempoExamenResponse
import com.crisordonez.registro.service.RangoTiempoExamenServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/rango-tiempo-examen")
class RangoTiempoExamenController(
    private val rangoService: RangoTiempoExamenServiceInterface
) {

    @GetMapping
    fun listar(
        @RequestParam(required = false) pregunta: String?,
        @RequestParam(required = false, defaultValue = "true") soloActivos: Boolean
    ): ResponseEntity<List<RangoTiempoExamenResponse>> =
        ResponseEntity.ok(rangoService.listar(pregunta, soloActivos))

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun crear(@Valid @RequestBody request: RangoTiempoExamenRequest): ResponseEntity<RangoTiempoExamenResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(rangoService.crear(request))

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{publicId}")
    fun actualizar(
        @PathVariable publicId: UUID,
        @Valid @RequestBody request: RangoTiempoExamenRequest
    ): ResponseEntity<RangoTiempoExamenResponse> =
        ResponseEntity.ok(rangoService.actualizar(publicId, request))

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminar(@PathVariable publicId: UUID) = rangoService.eliminar(publicId)
}
