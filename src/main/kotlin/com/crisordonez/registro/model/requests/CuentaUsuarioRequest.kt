package com.crisordonez.registro.model.requests

import jakarta.validation.constraints.NotNull

data class CuentaUsuarioRequest(

    @field:NotNull(message = "El nombre de usuario es requerido")
    val nombreUsuario: String,

    val fechaNacimientoCambioPass: String? = null,

    @field:NotNull(message = "La contrasena es requerida")
    val contrasena: String,

    val aceptaConsentimiento: Boolean = false,

    val rol: String = "USER",

    val paciente: PacienteRequest? = null,

    val appVersion: String? = null

)
