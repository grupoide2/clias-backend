package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.RangoTiempoExamenRequest
import com.crisordonez.registro.model.responses.RangoTiempoExamenResponse
import java.util.UUID

interface RangoTiempoExamenServiceInterface {

    /** Lista el catálogo; si [pregunta] no es nulo, filtra por esa pregunta. */
    fun listar(pregunta: String?, soloActivos: Boolean): List<RangoTiempoExamenResponse>

    fun crear(request: RangoTiempoExamenRequest): RangoTiempoExamenResponse

    fun actualizar(publicId: UUID, request: RangoTiempoExamenRequest): RangoTiempoExamenResponse

    /** Elimina el rango del catálogo. */
    fun eliminar(publicId: UUID)

    /** Valida que el código exista y esté activo; lanza BadRequestException si no. */
    fun validarCodigoActivo(codigo: String)
}
