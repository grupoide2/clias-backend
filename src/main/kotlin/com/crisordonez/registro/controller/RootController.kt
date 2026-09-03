package com.crisordonez.registro.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class RootController {

    @GetMapping("/")
    fun root(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to 200,
            "message" to "Bienvenido a la API CLIAS",
            "description" to "Servicio backend para la gestión de registros clínicos, usuarios, pacientes, exámenes, notificaciones y recursos relacionados.",
            "service" to "clias-backend",
            "version" to "1.0.0",
            "documentation" to "Consulte los endpoints disponibles en la documentación técnica del proyecto.",
            "health" to "/health",
            "timestamp" to LocalDateTime.now().toString()
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to 200,
            "message" to "El servicio se encuentra operativo.",
            "service" to "clias-backend",
            "timestamp" to LocalDateTime.now().toString()
        )

        return ResponseEntity.ok(response)
    }
}