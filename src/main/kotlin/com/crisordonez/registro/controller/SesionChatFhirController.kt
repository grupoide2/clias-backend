package com.crisordonez.registro.controller

import ca.uhn.fhir.context.FhirContext
import com.crisordonez.registro.model.mapper.SesionChatFhirMapper.toFhirBundle
import com.crisordonez.registro.repository.SesionChatRepository
import org.hl7.fhir.r4.model.Bundle
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/fhir/sesion-chat")
class SesionChatFhirController(
    private val repo: SesionChatRepository
) {

    private val fhirCtx: FhirContext = FhirContext.forR4()
    private val parser = fhirCtx.newJsonParser().setPrettyPrint(true)

    @GetMapping("/{publicId}", produces = ["application/fhir+json"])
    fun getOne(@PathVariable publicId: UUID): ResponseEntity<String> {
        val entity = repo.findByPublicId(publicId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión de chat no encontrada") }

        val bundle = toFhirBundle(entity)
        val json = parser.encodeResourceToString(bundle)
        return ResponseEntity.ok(json)
    }

    @GetMapping("/all", produces = ["application/fhir+json"])
    fun getAll(): ResponseEntity<String> {
        val master = Bundle().apply { type = Bundle.BundleType.COLLECTION }

        val all = repo.findAll()
        all.forEach { s ->
            val child = toFhirBundle(s)
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
