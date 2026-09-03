package com.crisordonez.registro.model.errors

import org.springframework.http.HttpStatus

data class AppErrorResponse(
    val tipo: HttpStatus,
    val mensaje: String
)