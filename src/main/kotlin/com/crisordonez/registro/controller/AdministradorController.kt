package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.CreateUserRequest
import com.crisordonez.registro.model.requests.UpdateUserRequest
import com.crisordonez.registro.model.responses.AdministradorResponse
import com.crisordonez.registro.service.AdministradorService
import com.crisordonez.registro.service.MedicoService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
class AdministradorController(
    private val service: AdministradorService,
    private val medicoService: MedicoService
) {

    @PostMapping("/users")
    fun createUser(@RequestBody req: CreateUserRequest): ResponseEntity<AdministradorResponse> =
        ResponseEntity.status(201).body(AdministradorResponse.from(service.create(req.nombre, req.usuario, req.contrasena)))

    @GetMapping("/users")
    fun listUsers(): List<AdministradorResponse> =
        service.listAll().map { AdministradorResponse.from(it) }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<AdministradorResponse> =
        service.getById(id)?.let { ResponseEntity.ok(AdministradorResponse.from(it)) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/users/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody req: UpdateUserRequest
    ): ResponseEntity<AdministradorResponse> =
        service.update(id, req.nombre, req.usuario, req.contrasena)
            ?.let { ResponseEntity.ok(AdministradorResponse.from(it)) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
