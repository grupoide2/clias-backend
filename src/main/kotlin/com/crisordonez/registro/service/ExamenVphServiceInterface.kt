// ExamenVphServiceInterface.kt
package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.ExamenResultadoRequest
import com.crisordonez.registro.model.responses.ExamenVphResponse
import org.springframework.web.multipart.MultipartFile

interface ExamenVphServiceInterface {

    //  Devuelve el nombre completo del paciente asociado a un examen, usando el código de dispositivo en la tabla examen_vph.
    fun obtenerNombrePorCodigo(codigoDispositivo: String): String

    fun establecerResultadoPrueba(publicId: String, pruebaRequest: ExamenResultadoRequest)

    fun getPrueba(publicId: String): ExamenVphResponse

    fun getTodasPruebas(): List<ExamenVphResponse>

    fun subirResultadoPdf(
        archivo: MultipartFile,
        nombre: String,
        dispositivo: String,
        diagnostico: String,
        genotiposStr: String?
    )
    // Vacía solo los campos de contenido, fechaResultado, nombre, tamano, tipo y diagnostico del registro identificado por el código de dispositivo.
    fun clearExamenFields(codigoDispositivo: String)


    fun getDevicePrefixes(): List<String>

    fun descargarPdf(codigoDispositivo: String): ByteArray

}