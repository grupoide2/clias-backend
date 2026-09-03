package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.AnamnesisRequest
import com.crisordonez.registro.model.responses.AnamnesisResponse
import com.crisordonez.registro.service.AnamnesisServiceInterface
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/anamnesis")
@PreAuthorize("hasRole('ADMIN')")
class AnamnesisController(
    private val anamnesisServiceInterface: AnamnesisServiceInterface
) {

    @PostMapping("/admin/{publicId}")
    fun crearAnamnesis(
        @PathVariable publicId: UUID,
        @Valid @RequestBody anamnesis: AnamnesisRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(anamnesisServiceInterface.crearAnamnesis(publicId, anamnesis))

    @GetMapping("/admin/{publicId}")
    fun getAnamnesis(@PathVariable publicId: UUID): ResponseEntity<AnamnesisResponse> =
        ResponseEntity.ok(anamnesisServiceInterface.getAnamnesis(publicId))

    @GetMapping("/admin")
    fun getTodasAnamnesis(): ResponseEntity<List<AnamnesisResponse>> =
        ResponseEntity.ok(anamnesisServiceInterface.getTodasAnamnesis())

    @PutMapping("/admin/{publicId}")
    fun editarAnamnesis(
        @PathVariable publicId: UUID,
        @Valid @RequestBody anamnesis: AnamnesisRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(anamnesisServiceInterface.editarAnamnesis(publicId, anamnesis))

    @DeleteMapping("/admin/{publicId}")
    fun eliminarAnamnesis(@PathVariable publicId: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(anamnesisServiceInterface.eliminarAnamnesis(publicId))
}
