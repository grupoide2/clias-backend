package com.crisordonez.registro.model.errors

import org.springframework.http.HttpStatus

class NotFoundException(override val message: String) : AppException(httpStatus = HttpStatus.NOT_FOUND, message)

class ConflictException(override val message: String) : AppException(httpStatus = HttpStatus.CONFLICT, message)

class BadRequestException(override val message: String) : AppException(httpStatus = HttpStatus.BAD_REQUEST, message)

class UnauthozionatedException(override val message: String) : AppException(httpStatus = HttpStatus.UNAUTHORIZED, message)