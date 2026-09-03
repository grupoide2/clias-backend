package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.OcupacionRequest
import com.crisordonez.registro.model.responses.OcupacionResponse
import java.util.UUID

interface OcupacionServiceInterface {

    fun listar(soloActivas: Boolean, soloUniversidad: Boolean?): List<OcupacionResponse>

    fun crear(request: OcupacionRequest): OcupacionResponse

    fun actualizar(publicId: UUID, request: OcupacionRequest): OcupacionResponse

    /** Elimina la ocupación del catálogo. */
    fun eliminar(publicId: UUID)
}
