package com.crisordonez.registro.controller

import com.crisordonez.registro.model.responses.SaludSexualResponse
import com.crisordonez.registro.service.SaludSexualServiceInterface
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/salud-sexual")
class SaludSexualController(
    private val saludSexualServiceInterface: SaludSexualServiceInterface
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{publicId}")
    fun getSaludSexual(@PathVariable publicId: UUID): ResponseEntity<SaludSexualResponse> =
        ResponseEntity.ok(saludSexualServiceInterface.getSaludSexual(publicId))

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodoSaludSexual(): ResponseEntity<List<SaludSexualResponse>> =
        ResponseEntity.ok(saludSexualServiceInterface.getTodosSaludSexual())
}
