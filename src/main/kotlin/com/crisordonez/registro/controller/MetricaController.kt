package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.AutomuestreoCompletadoRequest
import com.crisordonez.registro.model.responses.MetricaDashboardResponse
import com.crisordonez.registro.service.MetricaUsoServiceInterface
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/metricas")
class MetricaController(
    private val metricaUsoService: MetricaUsoServiceInterface
) {

    /**
     * La paciente completó el formulario de automuestreo ("Automuestreo completado").
     * Cuenta como sesión de chatbot exitosa (contador combinado).
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/automuestreo/completado")
    fun automuestreoCompletado(
        @RequestBody request: AutomuestreoCompletadoRequest
    ): ResponseEntity<Unit> {
        metricaUsoService.registrarAutomuestreoCompletado(request.cuentaPublicId)
        return ResponseEntity.ok().build()
    }

    /** Indicadores agregados de uso (panel administrativo). */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    fun dashboard(): ResponseEntity<MetricaDashboardResponse> =
        ResponseEntity.ok(metricaUsoService.dashboard())
}
