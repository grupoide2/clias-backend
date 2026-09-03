package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.SexoEnum
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MedicoRequest(

    @field:NotNull(message = "El usuario no puede ser nulo")
    @field:NotBlank(message = "El usuario no puede estar vacío")
    @field:Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    val usuario: String,

    @field:NotNull(message = "La contraseña no puede ser nula")
    @field:NotBlank(message = "La contraseña no puede estar vacía")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    val contrasena: String,

    @field:NotNull(message = "El nombre no puede ser nulo")
    @field:NotBlank(message = "El nombre no puede estar vacío")
    val nombre: String,

    @field:NotNull(message = "El correo no puede ser nulo")
    @field:NotBlank(message = "El correo no puede estar vacío")
    val correo: String,

    val especializacion: String? = null,

    @field:NotNull(message = "El sexo no puede ser nulo")
    val sexo: SexoEnum,

    @JsonProperty("n_registro")
    @field:NotNull(message = "El número de registro no puede ser nulo")
    @field:NotBlank(message = "El número de registro no puede estar vacío")
    @field:Size(min = 3, max = 20, message = "El número de registro debe tener entre 3 y 20 caracteres")
    val nRegistro: String = ""

)
