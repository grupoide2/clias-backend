package com.crisordonez.registro.model.requests

import com.crisordonez.registro.model.enums.SexoEnum

data class CreateMedicoRequest(
    val nombre: String,
    val correo: String,
    val especializacion: String,
    val sexo: SexoEnum,
    val usuario: String,
    val contrasena: String
)
data class UpdateMedicoRequest(
    val nombre: String,
    val correo: String,
    val especializacion: String,
    val sexo: SexoEnum,
    val usuario: String,
    val contrasena: String
)
data class DeleteMedicoRequest(
    val nombre: String,
    val correo: String,
    val especializacion: String,
    val sexo: SexoEnum,
    val usuario: String,
    val contrasena: String
)
