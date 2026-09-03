package com.crisordonez.registro.controller

import com.crisordonez.registro.model.dtos.EstadoDispositivoDto
import com.crisordonez.registro.service.EstadoDispositivoService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
@RequestMapping("/api/estado-dispositivos")
class EstadoDispositivoController(
    private val estadoDispositivoService: EstadoDispositivoService
) {

    @GetMapping
    fun getEstadoDispositivos(
        @RequestParam(required = false) fechaInicio: String?,
        @RequestParam(required = false) fechaFin: String?,
        @RequestParam(required = false) estado: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<EstadoDispositivoDto>> {
        val inicio = fechaInicio?.let { LocalDateTime.of(LocalDate.parse(it), LocalTime.MIN) }
        val fin = fechaFin?.let { LocalDateTime.of(LocalDate.parse(it), LocalTime.MAX) }

        return ResponseEntity.ok(
            estadoDispositivoService.getEstadoDispositivos(
                fechaInicio = inicio,
                fechaFin = fin,
                estado = estado,
                page = page,
                size = size
            )
        )
    }
}
