package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.UbicacionRequest
import com.crisordonez.registro.model.enums.EstablecimientoEnum
import com.crisordonez.registro.model.responses.UbicacionResponse
import com.crisordonez.registro.service.UbicacionServiceInterface
import com.crisordonez.registro.model.mapper.UbicacionMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/ubicaciones")
class UbicacionController(
    private val ubicacionService: UbicacionServiceInterface
) {

    @GetMapping
    fun obtenerUbicaciones(
        @RequestParam(required = false) establecimiento: EstablecimientoEnum?
    ): List<UbicacionResponse> {
        val ubicaciones = if (establecimiento != null) {
            ubicacionService.listarPorEstablecimiento(establecimiento)
        } else {
            ubicacionService.obtenerTodas()
        }
        return UbicacionMapper.toResponseList(ubicaciones ?: emptyList())
    }

    @PostMapping
    fun crearUbicacion(@RequestBody ubicacionRequest: UbicacionRequest): ResponseEntity<*> {
        return try {
            val entity = ubicacionService.crearUbicacion(UbicacionMapper.toEntity(ubicacionRequest))
            ResponseEntity.ok(UbicacionMapper.toResponse(entity))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("mensaje" to e.message))
        }
    }

    @PostMapping("/lote")
    fun crearUbicaciones(@RequestBody ubicacionesRequest: List<UbicacionRequest>): Map<String, Any> {
        val entidades = ubicacionesRequest.map { UbicacionMapper.toEntity(it) }
        val resultado = ubicacionService.crearUbicaciones(entidades)

        return mapOf(
            "creadas" to UbicacionMapper.toResponseList(resultado.creadas),
            "rechazadas" to resultado.rechazadas
        )
    }

    @PutMapping("/{publicId}")
    fun actualizarUbicacion(
        @PathVariable publicId: UUID,
        @Valid @RequestBody ubicacionRequest: UbicacionRequest
    ): UbicacionResponse? {
        val entity = ubicacionService.actualizarUbicacion(publicId, UbicacionMapper.toEntity(ubicacionRequest))
        return entity?.let { UbicacionMapper.toResponse(it) }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{publicId}")
    fun eliminarUbicacion(@PathVariable publicId: UUID) {
        ubicacionService.eliminarUbicacion(publicId)
    }
}
