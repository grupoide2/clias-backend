package com.crisordonez.registro.model.requests

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class FolletoRequest(

    // ── Datos de la paciente ──────────────────────────────────────────────────

    @field:NotBlank(message = "El nombre es requerido")
    @field:Size(max = 200)
    val nombre: String,

    @field:NotBlank(message = "El apellido es requerido")
    @field:Size(max = 200)
    val apellido: String,

    @field:NotBlank(message = "El teléfono es requerido")
    @field:Size(min = 7, max = 20)
    val telefono: String,

    @field:NotBlank(message = "La fecha de nacimiento es requerida")
    val fechaNacimiento: String,       // formato dd/MM/yyyy

    @field:NotBlank(message = "El país es requerido")
    val pais: String,

    @field:NotBlank(message = "La zona residencial es requerida")
    val zonaResidencial: String,

    @field:NotBlank(message = "La lengua materna es requerida")
    val lenguaMaterna: String,

    @field:NotBlank(message = "El estado civil es requerido")
    val estadoCivil: String,

    @field:NotBlank(message = "El sexo es requerido")
    val sexo: String,

    // ── Datos de salud sexual (mismas preguntas que el formulario de la app) ──

    @field:NotNull(message = "Los datos de salud sexual son requeridos")
    @field:Valid
    val saludSexual: SaludSexualRequest,

    // ── Información socioeconómica (opcional) ────────────────────────────────

    val informacionSocioeconomica: FolletoInfoSocioeconomicaRequest? = null,

    // ── Dispositivo QR (código digitado por la secretaria) ───────────────────

    @field:NotBlank(message = "El código del dispositivo QR es requerido")
    val dispositivo: String,

    @field:NotBlank(message = "La fecha del examen es requerida")
    val fechaExamen: String,           // ISO-8601 (LocalDateTime)

    val fechaRegistro: String? = null  // ISO-8601 (LocalDateTime) — null → usa hora del servidor
)
