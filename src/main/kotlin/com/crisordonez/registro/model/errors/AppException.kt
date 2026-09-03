package com.crisordonez.registro.model.errors

import org.springframework.http.HttpStatus

open class AppException(
    val httpStatus: HttpStatus,
    override val message: String
) : RuntimeException(message)