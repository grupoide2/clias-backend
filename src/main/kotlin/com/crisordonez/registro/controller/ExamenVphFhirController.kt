package com.crisordonez.registro.controller

import ca.uhn.fhir.context.FhirContext
import com.crisordonez.registro.model.mapper.ExamenVphFhirMapper.toFhirBundle
import com.crisordonez.registro.repository.ExamenVphRepository
import org.hl7.fhir.r4.model.Bundle
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/fhir/examen-vph")
class ExamenVphFhirController(
    private val examenRepo: ExamenVphRepository
) {

    private val fhirCtx = FhirContext.forR4()
    private val parser = fhirCtx.newJsonParser().setPrettyPrint(true)

    @GetMapping("/{publicId}", produces = ["application/fhir+json"])
    fun getOne(@PathVariable publicId: UUID): ResponseEntity<String> {
        val ex = examenRepo.findByPublicId(publicId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Examen no encontrado")

        val bundle = toFhirBundle(ex)
        val json = parser.encodeResourceToString(bundle)
        return ResponseEntity.ok(json)
    }

    @GetMapping("/all", produces = ["application/fhir+json"])
    fun getAll(): ResponseEntity<String> {
        val exams = examenRepo.findAll()

        val master = Bundle().apply { type = Bundle.BundleType.COLLECTION }

        exams.forEach { ex ->
            val child = toFhirBundle(ex)
            child.entry.forEach { e ->
                master.addEntry(Bundle.BundleEntryComponent().apply {
                    fullUrl = e.fullUrl
                    resource = e.resource
                })
            }
        }

        val json = parser.encodeResourceToString(master)
        return ResponseEntity.ok(json)
    }
}
