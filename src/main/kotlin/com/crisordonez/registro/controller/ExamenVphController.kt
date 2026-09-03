package com.crisordonez.registro.controller

import com.crisordonez.registro.model.requests.ExamenResultadoRequest
import com.crisordonez.registro.model.responses.ExamenVphResponse
import com.crisordonez.registro.service.ExamenVphServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/prueba")
class ExamenVphController(
    private val examenVphServiceInterface: ExamenVphServiceInterface
) {

    @GetMapping("/medico/nombre/{codigoDispositivo}")
    fun obtenerNombrePaciente(@PathVariable codigoDispositivo: String): ResponseEntity<String> =
        ResponseEntity.ok(examenVphServiceInterface.obtenerNombrePorCodigo(codigoDispositivo))

    @PutMapping("/admin/resultado/{publicId}")
    fun establecerResultado(
        @PathVariable publicId: String,
        @Valid @RequestBody prueba: ExamenResultadoRequest
    ): ResponseEntity<Unit> {
        examenVphServiceInterface.establecerResultadoPrueba(publicId, prueba)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/admin/{publicId}")
    fun getPrueba(@PathVariable publicId: String): ResponseEntity<ExamenVphResponse> =
        ResponseEntity.ok(examenVphServiceInterface.getPrueba(publicId))

    @GetMapping("/admin")
    fun getTodasPruebas(): ResponseEntity<List<ExamenVphResponse>> =
        ResponseEntity.ok(examenVphServiceInterface.getTodasPruebas())

    @PostMapping("/medico/subir")
    fun subirExamenSinPacienteId(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("nombre") nombre: String,
        @RequestParam("dispositivo") dispositivo: String,
        @RequestParam("diagnostico") diagnostico: String,
        @RequestParam("genotipos", required = false) genotiposStr: String?
    ): ResponseEntity<String> {
        examenVphServiceInterface.subirResultadoPdf(
            archivo = file,
            nombre = nombre,
            dispositivo = dispositivo,
            diagnostico = diagnostico,
            genotiposStr = genotiposStr
        )
        return ResponseEntity.ok("Examen subido correctamente")
    }

    @PatchMapping("/medico/clear-fields/{codigoDispositivo}")
    fun clearExamenFields(@PathVariable codigoDispositivo: String): ResponseEntity<Void> {
        examenVphServiceInterface.clearExamenFields(codigoDispositivo)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/medico/prefixes")
    fun getDevicePrefixes(): ResponseEntity<List<String>> =
        ResponseEntity.ok(examenVphServiceInterface.getDevicePrefixes())

    @GetMapping("/medico/pdf/{codigoDispositivo}")
    fun descargarPdf(@PathVariable codigoDispositivo: String): ResponseEntity<ByteArray> {
        val bytes = examenVphServiceInterface.descargarPdf(codigoDispositivo)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"examen_$codigoDispositivo.pdf\"")
            .body(bytes)
    }
}
