package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.EncuestaSusRequest
import com.crisordonez.registro.service.EncuestaSusServiceInterface
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/encuesta_sus")
@PreAuthorize("hasRole('USER')")
class EncuestaSusController(
    private val encuestaService: EncuestaSusServiceInterface
) {

    @PostMapping
    fun guardarEncuesta(@RequestBody request: EncuestaSusRequest): ResponseEntity<Any> =
        if (encuestaService.guardarEncuesta(request)) ResponseEntity.ok("Encuesta guardada correctamente")
        else ResponseEntity.badRequest().body("No se pudo guardar la encuesta")

    @GetMapping("/completada/{cuentaUsuarioId}")
    fun verificarEncuestaCompletada(@PathVariable cuentaUsuarioId: String): ResponseEntity<Boolean> =
        ResponseEntity.ok(encuestaService.estaEncuestaCompletada(cuentaUsuarioId))
}
