// src/main/kotlin/com/crisordonez/registro/model/requests/CreateUserRequest.kt
package com.crisordonez.registro.model.requests

data class CreateUserRequest(
    val nombre: String,
    val usuario: String,
    val contrasena: String
)

data class UpdateUserRequest(
    val nombre: String,
    val usuario: String,
    val contrasena: String
)

data class DeleteUserRequest(
    val nombre: String,
    val usuario: String,
    val contrasena: String
)
