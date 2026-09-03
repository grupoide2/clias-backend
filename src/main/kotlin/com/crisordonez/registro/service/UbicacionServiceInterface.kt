package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.UbicacionEntity
import com.crisordonez.registro.service.UbicacionService.ResultadoCargarUbicaciones
import com.crisordonez.registro.model.enums.EstablecimientoEnum
import java.util.UUID

interface UbicacionServiceInterface {

    fun obtenerTodas(): List<UbicacionEntity>

    fun listarPorEstablecimiento(establecimiento: EstablecimientoEnum): List<UbicacionEntity>?

    fun crearUbicacion(ubicacion: UbicacionEntity): UbicacionEntity

    fun crearUbicaciones(ubicaciones: List<UbicacionEntity>): ResultadoCargarUbicaciones

    fun actualizarUbicacion(publicId: UUID, ubicacion: UbicacionEntity): UbicacionEntity?

    fun eliminarUbicacion(publicId: UUID)
}
