package com.crisordonez.registro.controller

import com.crisordonez.registro.service.CodigoQRService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.crisordonez.registro.model.responses.CodigoQRStatusResponse
import com.crisordonez.registro.model.responses.CodigoQRValidacionResponse

@RestController
@RequestMapping("/api/codigosqr")
class CodigoQRController(
    private val service: CodigoQRService
) {

    @PostMapping
    fun guardarCodigo(
        @RequestParam codigo: String,
        @RequestParam fechaExpiracion: String
    ): ResponseEntity<String> {
        service.guardar(codigo, fechaExpiracion)
        return ResponseEntity.status(201).body("Código QR guardado exitosamente")
    }

    @GetMapping
    fun listarCodigos(@RequestParam(required = false) status: String?): ResponseEntity<List<CodigoQRStatusResponse>> {
        val lista = service.obtenerTodosConStatus()
        val filtrado = if (status == null || status == "todos") lista else lista.filter { it.status == status }
        return ResponseEntity.ok(filtrado)
    }

    /** Valida un código escaneado contra la tabla codigos_qr. */
    @GetMapping("/validar/{codigo}")
    fun validarCodigo(@PathVariable codigo: String): ResponseEntity<CodigoQRValidacionResponse> =
        ResponseEntity.ok(service.validar(codigo))


}

