package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.UbicacionEntity
import com.crisordonez.registro.model.enums.EstablecimientoEnum
import com.crisordonez.registro.model.responses.UbicacionRechazadaResponse
import com.crisordonez.registro.repository.UbicacionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.math.*

@Service
class UbicacionService(
    private val ubicacionRepository: UbicacionRepository
) : UbicacionServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun obtenerTodas(): List<UbicacionEntity> {
        return ubicacionRepository.findAll()
    }

    override fun listarPorEstablecimiento(establecimiento: EstablecimientoEnum): List<UbicacionEntity>? {
        return ubicacionRepository.findByEstablecimiento(establecimiento)
    }

    override fun crearUbicacion(ubicacion: UbicacionEntity): UbicacionEntity {
        if (existeUbicacionCercana(ubicacion.latitud, ubicacion.longitud, ubicacion.establecimiento)) {
            throw IllegalArgumentException("Ya existe una ubicación cercana en ese punto.")
        }

        return ubicacionRepository.save(ubicacion)
    }

    data class ResultadoCargarUbicaciones(
        val creadas: List<UbicacionEntity>,
        val rechazadas: List<UbicacionRechazadaResponse>
    )


    override fun crearUbicaciones(ubicaciones: List<UbicacionEntity>): ResultadoCargarUbicaciones {
        val creadas = mutableListOf<UbicacionEntity>()
        val rechazadas = mutableListOf<UbicacionRechazadaResponse>()
        log.info("Creando ubicaciones en lote, total: ${ubicaciones.size}")
        for (ubicacion in ubicaciones) {
            if (existeUbicacionCercana(ubicacion.latitud, ubicacion.longitud, ubicacion.establecimiento)) {
                rechazadas.add(
                    UbicacionRechazadaResponse(
                        nombre = ubicacion.nombre,
                        direccion = ubicacion.direccion,
                        motivo = "Ubicación cercana ya registrada"
                    )
                )
            } else {
                val guardada = ubicacionRepository.save(ubicacion)
                creadas.add(guardada)
            }
        }
        log.info("Ubicaciones creadas: ${creadas.size}, rechazadas: ${rechazadas.size}")
        return ResultadoCargarUbicaciones(creadas, rechazadas)
    }

    @Transactional
    override fun actualizarUbicacion(publicId: UUID, ubicacion: UbicacionEntity): UbicacionEntity? {
        log.info("Actualizando ubicación con publicId: $publicId")
        val existente = ubicacionRepository.findByPublicId(publicId) ?: return null

        existente.nombre = ubicacion.nombre
        existente.telefono = ubicacion.telefono
        existente.direccion = ubicacion.direccion
        existente.horario = ubicacion.horario
        existente.sitioWeb = ubicacion.sitioWeb
        existente.latitud = ubicacion.latitud
        existente.longitud = ubicacion.longitud
        existente.establecimiento = ubicacion.establecimiento
        existente.updatedAt = OffsetDateTime.now()

        log.info("Ubicación actualizada correctamente: $existente")

        return ubicacionRepository.save(existente)
    }

    @Transactional
    override fun eliminarUbicacion(publicId: UUID) {
        log.info("Eliminando ubicación con publicId: $publicId")
        ubicacionRepository.deleteByPublicId(publicId)
        log.info("Ubicación eliminada correctamente")
    }

    private fun calcularDistanciaMetros(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val radioTierra = 6371000.0 // en metros

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierra * c
    }

    private fun existeUbicacionCercana(
        lat: Double,
        lon: Double,
        establecimiento: EstablecimientoEnum,
        umbralMetros: Double = 10.0
    ): Boolean {
        val ubicaciones = ubicacionRepository.findByEstablecimiento(establecimiento)
        return ubicaciones.any {
            calcularDistanciaMetros(lat, lon, it.latitud, it.longitud) < umbralMetros
        }
    }
}
