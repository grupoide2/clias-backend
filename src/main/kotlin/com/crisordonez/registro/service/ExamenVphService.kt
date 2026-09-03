// ExamenVphService.kt
package com.crisordonez.registro.service


import com.crisordonez.registro.model.entities.ExamenVphEntity
import com.crisordonez.registro.model.entities.PacienteEntity
import com.crisordonez.registro.model.enums.TipoArchivoEnum
import com.crisordonez.registro.model.requests.ExamenResultadoRequest
import com.crisordonez.registro.model.responses.ExamenVphResponse
import com.crisordonez.registro.repository.*
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.EvolucionMapper.toEntity
import com.crisordonez.registro.model.mapper.ExamenVphMapper.toResponse
import com.crisordonez.registro.model.mapper.ExamenVphMapper.toUpdateResultado
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.Date



@Service
class ExamenVphService(
    @Autowired private val examenVphRepository: ExamenVphRepository,
    @Autowired private val evolucionRepository: EvolucionRepository,
    @Autowired private val saludSexualRepository: SaludSexualRepository,
    @Autowired private val sesionChatRepository: SesionChatRepository,
    @Autowired private val dispositivoRegistradoRepository: DispositivoRegistradoRepository
) : ExamenVphServiceInterface {

    private val log = LoggerFactory.getLogger(this.javaClass)

     override fun obtenerNombrePorCodigo(codigoDispositivo: String): String {
        log.info("Usando SESION_CHAT para buscar paciente del dispositivo: $codigoDispositivo")

        // 1) Buscar examen en examen_vph por código de dispositivo
        val examen: ExamenVphEntity = examenVphRepository
            .findByDispositivo(codigoDispositivo)
            .orElseThrow { NotFoundException("Examen con código '$codigoDispositivo' no encontrado") }

        // 2) Obtener la entidad SesionChat relacionada
        val sesionChat = examen.sesionChat

        // 3) De la entidad SesionChat obtener el paciente y su nombre
        val paciente: PacienteEntity = sesionChat.paciente

        return paciente.nombre
    }


    override fun establecerResultadoPrueba(publicId: String, pruebaRequest: ExamenResultadoRequest) {
        log.info("Estableciendo resultado - Prueba: $publicId")
        val prueba = examenVphRepository.findByDispositivo(publicId).orElseThrow {
            NotFoundException("No existe un examen relacionado al dispositivo $publicId")
        }

        val evolucion = pruebaRequest.evolucion?.let {
            evolucionRepository.save(it.toEntity(prueba))
        }

        examenVphRepository.save(prueba.toUpdateResultado(pruebaRequest, evolucion))

        log.info("Resultado establecido correctamente")
    }

    override fun getPrueba(publicId: String): ExamenVphResponse {
        log.info("Consultando prueba por dispositivo: $publicId")
        val prueba = examenVphRepository.findByDispositivo(publicId).orElseThrow {
            NotFoundException("No existe prueba relacionada con el dispositivo $publicId")
        }
        log.info("Prueba consultada correctamente")

        return prueba.toResponse()
    }

    override fun getTodasPruebas(): List<ExamenVphResponse> {
        log.info("Consultando todas las pruebas")
        return examenVphRepository.findAll().map { it.toResponse() }
    }

    override fun subirResultadoPdf(
        archivo: MultipartFile,
        nombre: String,
        dispositivo: String,
        diagnostico: String,
        genotiposStr: String?
    ) {
        try {
            log.info("Subiendo resultado PDF para dispositivo $dispositivo")

            val dispositivoEnt = dispositivoRegistradoRepository
                .findByDispositivo(dispositivo)
                .orElseThrow { Exception("No se encontró el dispositivo con código $dispositivo") }

            val examen = examenVphRepository.findByDispositivo(dispositivo)
                .orElseThrow { Exception("No se encontró examen para el dispositivo $dispositivo") }

            val sesionChat = examen.sesionChat
                ?: throw Exception("El examen no tiene sesión de chat asociada")

            val genotipos: List<String> = genotiposStr
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    jacksonObjectMapper().readValue(
                        it,
                        object : com.fasterxml.jackson.core.type.TypeReference<List<String>>() {}
                    )
                }
                ?: emptyList()

            examen.apply {
                contenido      = archivo.bytes
                this.nombre    = nombre
                this.diagnostico = diagnostico
                tamano         = archivo.size.toLong()
                tipo           = TipoArchivoEnum.PDF
                this.genotipos = genotipos
                fechaResultado = Date()
            }
            examenVphRepository.save(examen)
            log.info("Resultado actualizado correctamente")


        } catch (e: Exception) {
            log.error("Error al subir resultado: ${e.message}")
            throw e
        }
    }


    // Vacía solo los campos de contenido, fechaResultado, nombre, tamano, tipo y diagnostico del registro identificado por el código de dispositivo.
    override fun clearExamenFields(codigoDispositivo: String) {
        log.info("Limpiando campos del examen VPH para dispositivo: $codigoDispositivo")
        val updated = examenVphRepository.clearFieldsByCodigo(codigoDispositivo)
        if (updated == 0) {
            throw NoSuchElementException("No se encontró examen con código $codigoDispositivo para limpiar campos")
        }
        log.info("Campos vaciados correctamente ($updated fila(s) afectada(s))")
    }

    override fun descargarPdf(codigoDispositivo: String): ByteArray {
        log.info("Descargando PDF del examen para dispositivo: $codigoDispositivo")
        val examen = examenVphRepository.findFirstByDispositivo(codigoDispositivo).orElseThrow {
            NotFoundException("No existe examen para el dispositivo $codigoDispositivo")
        }
        return examen.contenido
            ?: throw NotFoundException("El examen del dispositivo $codigoDispositivo aún no tiene PDF subido")
    }

    // Devuelve la lista de prefijos (p.ej. "010151-") desde dispositivos_registrados.dispositivo
    override fun getDevicePrefixes(): List<String> {
        val registros = dispositivoRegistradoRepository.findAll()
        log.info("Total dispositivos registrados: ${registros.count()}")
        return registros
            .map { dr ->
                // extrae hasta el guión incluido
                val code = dr.dispositivo
                val dashIndex = code.indexOf('-')
                if (dashIndex >= 0) code.substring(0, dashIndex + 1) else code
            }
            .distinct()
            .also { log.info("Prefijos calculados: $it") }
    }

}