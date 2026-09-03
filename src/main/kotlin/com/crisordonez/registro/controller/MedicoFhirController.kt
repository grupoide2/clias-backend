
// src/main/kotlin/com/crisordonez/registro/controller/MedicoFhirController.kt
package com.crisordonez.registro.controller

import com.crisordonez.registro.model.mapper.MedicoFhirMapper.mapMedicoToFhir
import com.crisordonez.registro.repository.MedicoRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import java.util.UUID
import ca.uhn.fhir.context.FhirContext
import org.hl7.fhir.r4.model.Bundle

@RestController
@RequestMapping("/fhir/medico")
class MedicoFhirController(
    private val medicoRepository: MedicoRepository
) {
    private val fhirCtx = FhirContext.forR4()
    private val parser = fhirCtx.newJsonParser().setPrettyPrint(true)

    @GetMapping("/{publicId}", produces = ["application/fhir+json"])
    fun findByPublicId(@PathVariable publicId: UUID): ResponseEntity<String> {
        val medico = medicoRepository.findByPublicId(publicId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado")

        val practitioner = mapMedicoToFhir(medico)
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            addEntry().resource = practitioner
        }
        val json = parser.encodeResourceToString(bundle)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/fhir+json"))
            .body(json)
    }

    @GetMapping("/all", produces = ["application/fhir+json"])
    fun findAll(): ResponseEntity<String> {
        val bundle = Bundle().apply { type = Bundle.BundleType.COLLECTION }
        medicoRepository.findAll().forEach { m ->
            bundle.addEntry().resource = mapMedicoToFhir(m)
        }
        val json = parser.encodeResourceToString(bundle)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/fhir+json"))
            .body(json)
    }
}
