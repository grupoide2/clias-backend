package com.crisordonez.registro.configuration

import com.crisordonez.registro.model.errors.AppErrorResponse
import com.crisordonez.registro.model.errors.AppException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class AppExceptionHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException): ResponseEntity<AppErrorResponse> {
        log.warn(ex.message)
        val response = AppErrorResponse(tipo = ex.httpStatus, mensaje = ex.message)
        return ResponseEntity(response, ex.httpStatus)
    }

    //  Cuando no existe handler ni recurso estático → 404 (no 500)
    @ExceptionHandler(NoResourceFoundException::class, NoHandlerFoundException::class)
    fun handleNotFound(): ResponseEntity<Void> {
        return ResponseEntity.notFound().build()
    }

    // Cualquier otro error real → 500
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<AppErrorResponse> {
        log.error("Error inesperado", ex)
        val response = AppErrorResponse(
            tipo = HttpStatus.INTERNAL_SERVER_ERROR,
            mensaje = "Ha ocurrido un error inesperado"
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
