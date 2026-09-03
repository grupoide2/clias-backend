package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.AppVersionRequest
import com.crisordonez.registro.model.requests.TiempoChatRequest
import com.crisordonez.registro.model.requests.TiempoAppRequest
import com.crisordonez.registro.model.requests.CuentaUsuarioRequest
import com.crisordonez.registro.model.responses.CuentaUsuarioResponse
import com.crisordonez.registro.service.CuentaUsuarioServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/usuarios")
class CuentaUsuarioController(
    private val cuentaUsuarioServiceInterface: CuentaUsuarioServiceInterface
) {

    // ── Públicos (permitAll en SecurityConfig) ────────────────────────────────

    @PostMapping("/registro")
    fun crearCuentaUsuario(@Valid @RequestBody cuenta: CuentaUsuarioRequest): ResponseEntity<CuentaUsuarioResponse> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.crearCuentaUsuario(cuenta))

    @PostMapping("/autenticar")
    fun autenticar(@Valid @RequestBody usuario: CuentaUsuarioRequest): ResponseEntity<CuentaUsuarioResponse> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.autenticar(usuario))

    @GetMapping("/validar")
    fun validarToken(@RequestHeader("token") token: String): ResponseEntity<String?> {
        val newToken = cuentaUsuarioServiceInterface.validarExpiracionToken(token)
        return if (newToken != null) ResponseEntity.ok(newToken)
        else ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN_EXPIRADO")
    }

    @PutMapping("/cambiar-contrasena")
    fun cambiarContrasena(@Valid @RequestBody cuenta: CuentaUsuarioRequest): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.updateContrasena(cuenta))

    @GetMapping("/public-indent/{idInterno}")
    fun obtenerPublicIdDesdeIdInterno(@PathVariable idInterno: Long): ResponseEntity<UUID> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.obtenerPublicIdPorIdInterno(idInterno))

    // ── Usuario autenticado (rol USER) ────────────────────────────────────────

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/editar/{publicId}")
    fun editarUsuario(
        @PathVariable publicId: UUID,
        @Valid @RequestBody cuenta: CuentaUsuarioRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.editarCuentaUsuario(publicId, cuenta))

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/chat-time/{publicId}")
    fun actualizarTimestampsChat(
        @PathVariable publicId: UUID,
        @Valid @RequestBody tiempoChatRequest: TiempoChatRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.updateTiempoChat(publicId, tiempoChatRequest))

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/app-time/{publicId}")
    fun actualizarTiempoApp(
        @PathVariable publicId: UUID,
        @RequestBody tiempoAppRequest: TiempoAppRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.updateTiempoApp(publicId, tiempoAppRequest))

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/app-version/{publicId}")
    fun establecerVersionApp(
        @PathVariable publicId: UUID,
        @RequestBody appVersion: AppVersionRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.stablishAppVersion(publicId, appVersion))

    // ── Administrador (rol ADMIN) ──────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{publicId}")
    fun getUsuario(@PathVariable publicId: UUID): ResponseEntity<CuentaUsuarioResponse> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.getCuentaUsuario(publicId))

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun getTodosUsuarios(): ResponseEntity<List<CuentaUsuarioResponse>> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.getAllCuentas())

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/eliminar/{publicId}")
    fun eliminarUsuario(@PathVariable publicId: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.eliminarCuentaUsuario(publicId))

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/chat-time-average")
    fun obtenerTiempoPromedioChat(): ResponseEntity<Double> =
        ResponseEntity.ok(cuentaUsuarioServiceInterface.getChatTimeAvergae())
}
