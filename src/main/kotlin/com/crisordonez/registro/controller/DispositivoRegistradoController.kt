package com.crisordonez.registro.controller

import com.crisordonez.registro.model.responses.DispositivoRegistradoResponse
import com.crisordonez.registro.service.DispositivoRegistradoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException

@RestController
@RequestMapping("/api/dispositivos_registrados")
class DispositivoRegistradoController(
    private val service: DispositivoRegistradoService
) {

    @GetMapping("/{dispositivo}")
    fun getByDispositivo(@PathVariable dispositivo: String): ResponseEntity<DispositivoRegistradoResponse> =
        try {
            ResponseEntity.ok(service.findByDispositivo(dispositivo))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
}
