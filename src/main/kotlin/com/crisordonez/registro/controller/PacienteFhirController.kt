package com.crisordonez.registro.controller

import com.crisordonez.registro.repository.PacienteRepository
import com.crisordonez.registro.utils.mapPacienteToFhir
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import ca.uhn.fhir.context.FhirContext
import org.hl7.fhir.r4.model.Bundle

@RestController
@RequestMapping("/fhir/paciente")
class PacienteFhirController(
    private val pacienteRepository: PacienteRepository
) {

    private val fhirCtx = FhirContext.forR4()
    private val parser = fhirCtx.newJsonParser().setPrettyPrint(true)

    @GetMapping("/{publicId}", produces = ["application/fhir+json"])
    fun findByPublicId(@PathVariable publicId: UUID): ResponseEntity<String> {
        val paciente = pacienteRepository.findByPublicId(publicId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado") }

        val bundle = mapPacienteToFhir(paciente)

        val json = parser.encodeResourceToString(bundle)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/fhir+json"))
            .body(json)
    }

    @GetMapping("/all", produces = ["application/fhir+json"])
    fun findAllFhirPacientes(): ResponseEntity<String> {
        val pacientes = pacienteRepository.findAll()

        val master = Bundle().apply { type = Bundle.BundleType.COLLECTION }
        pacientes.forEach { p ->
            val child = mapPacienteToFhir(p)
            child.entry.forEach { e ->
                master.addEntry(Bundle.BundleEntryComponent().apply {
                    fullUrl = e.fullUrl
                    resource = e.resource
                })
            }
        }

        val json = parser.encodeResourceToString(master)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/fhir+json"))
            .body(json)
    }
}
