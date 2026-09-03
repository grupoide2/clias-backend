package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.DispositivoAppUsuarioRequest
import com.crisordonez.registro.model.responses.DispositivoAppUsuarioResponse
import com.crisordonez.registro.service.DispositivoAppUsuarioServiceInterface
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/dispositivo")
class DispositivoAppUsuarioController {

    @Autowired
    lateinit var dispositivoAppUsuarioService: DispositivoAppUsuarioServiceInterface

    @PostMapping
    fun registrarDispositivo(
        @Valid @RequestBody request: DispositivoAppUsuarioRequest
    ): ResponseEntity<Unit> {
        dispositivoAppUsuarioService.registrarDispositivo(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{usuarioPublicId}")
    fun listarDispositivosPorUsuario(
        @PathVariable usuarioPublicId: UUID
    ): ResponseEntity<List<DispositivoAppUsuarioResponse>> {
        val dispositivos = dispositivoAppUsuarioService.listarDispositivosPorUsuario(usuarioPublicId)
        return ResponseEntity.ok(dispositivos)
    }
}