package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.NotificacionRequest
import com.crisordonez.registro.model.responses.NotificacionResponse
import com.crisordonez.registro.service.NotificacionServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/notificaciones")
class NotificationController(
    private val notificacionService: NotificacionServiceInterface
) {

    // ── Público — invocado por el scheduler interno y clias-notificaciones ────

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun crearNotificacion(@Valid @RequestBody request: NotificacionRequest): NotificacionResponse =
        notificacionService.crearNotificacion(request)

    // ── Usuario autenticado (rol USER) ────────────────────────────────────────

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{cuentaUsuarioPublicId}")
    fun obtenerHistorial(@PathVariable cuentaUsuarioPublicId: UUID): List<NotificacionResponse> =
        notificacionService.obtenerHistorialNotificaciones(cuentaUsuarioPublicId)

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{notificacionPublicId}/marcar-leida")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun marcarComoLeida(@PathVariable notificacionPublicId: UUID) =
        notificacionService.marcarNotificacionComoLeida(notificacionPublicId)

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/programada/desactivar-entrega/{cuentaUsuarioPublicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun desactivarRecordatorioEntregaDispositivo(@PathVariable cuentaUsuarioPublicId: UUID) =
        notificacionService.desactivarRecordatorioNoEntregaDispositivo(cuentaUsuarioPublicId)
}
