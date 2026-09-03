package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.CodigoQREntity
import com.crisordonez.registro.model.errors.AppException
import com.crisordonez.registro.model.responses.CodigoQRStatusResponse
import com.crisordonez.registro.model.responses.CodigoQRValidacionResponse
import com.crisordonez.registro.repository.CodigoQRRepository
import com.crisordonez.registro.repository.DispositivoRegistradoRepository
import com.crisordonez.registro.repository.ExamenVphRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CodigoQRService(
    private val codigoQRRepository: CodigoQRRepository,
    private val dispositivoRegistradoRepository: DispositivoRegistradoRepository,
    private val examenVphRepository: ExamenVphRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun guardar(codigo: String, fechaExpiracionStr: String): CodigoQREntity {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaExpiracion = LocalDate.parse(fechaExpiracionStr, formatter)

        if (codigoQRRepository.findByCodigo(codigo).isPresent) {
            throw AppException(HttpStatus.CONFLICT, "El código '$codigo' ya existe")
        }

        val entidad = CodigoQREntity(
            codigo = codigo,
            fechaExpiracion = fechaExpiracion
        )
        return codigoQRRepository.save(entidad)
    }

    fun validar(codigo: String): CodigoQRValidacionResponse {
        val limpio = codigo.trim()
        val entidad = codigoQRRepository.findByCodigo(limpio).orElse(null)
            ?: return CodigoQRValidacionResponse(
                valido = false,
                expirado = false,
                mensaje = "El código no corresponde a un dispositivo válido del sistema."
            )
        if (entidad.fechaExpiracion.isBefore(LocalDate.now())) {
            return CodigoQRValidacionResponse(
                valido = false,
                expirado = true,
                mensaje = "El código de este dispositivo está expirado."
            )
        }
        return CodigoQRValidacionResponse(valido = true, expirado = false, mensaje = "Código válido.")
    }

    fun obtenerTodosConStatus(): List<CodigoQRStatusResponse> {
        val codigos = codigoQRRepository.findAll()

        return codigos.mapNotNull { codigo ->
            try {
                val estaRegistrado = dispositivoRegistradoRepository.findFirstByDispositivo(codigo.codigo).isPresent
                val examenOpt = examenVphRepository.findFirstByDispositivo(codigo.codigo)

                val status = when {
                    examenOpt.isPresent -> {
                        val examen = examenOpt.get()
                        val tieneCamposLlenos = examen.fechaResultado != null &&
                                !examen.nombre.isNullOrBlank() &&
                                examen.tamano != null &&
                                examen.tipo != null &&
                                !examen.diagnostico.isNullOrBlank()
                        if (tieneCamposLlenos) "resultado listo" else "en proceso"
                    }
                    estaRegistrado -> "registrado"
                    else -> "generado"
                }

                CodigoQRStatusResponse(
                    codigo = codigo.codigo,
                    fechaExpiracion = codigo.fechaExpiracion,
                    status = status
                )
            } catch (e: Exception) {
                log.warn("Error al procesar código QR '${codigo.codigo}': ${e.message}")
                null
            }
        }
    }
}
