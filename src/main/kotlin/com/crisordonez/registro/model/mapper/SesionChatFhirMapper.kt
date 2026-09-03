package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.SesionChatEntity
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.utilities.xhtml.XhtmlNode
import java.time.ZoneId
import java.util.Date
import java.util.UUID

object SesionChatFhirMapper {

    fun toFhirBundle(s: SesionChatEntity): Bundle {
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            id = "Bundle/${UUID.randomUUID()}"
        }

        val patientRef = buildPatientReference(s)

        val encounter = buildEncounter(s, patientRef)
        val comm = buildCommunication(s, patientRef, encounter)

        bundle.addEntry(Bundle.BundleEntryComponent().apply {
            fullUrl = "urn:uuid:${encounter.idElement.idPart}"
            resource = encounter
        })
        bundle.addEntry(Bundle.BundleEntryComponent().apply {
            fullUrl = "urn:uuid:${comm.idElement.idPart}"
            resource = comm
        })

        return bundle
    }

    private fun buildPatientReference(s: SesionChatEntity): Reference {
        val pid = try { s.paciente?.publicId?.toString() } catch (_: Exception) { null }
        return if (!pid.isNullOrBlank()) Reference("Patient/$pid")
        else Reference().apply { display = "Paciente no resuelto" }
    }

    private fun buildEncounter(s: SesionChatEntity, patientRef: Reference): Encounter {
        val enc = Encounter()
        enc.id = "Encounter/${UUID.randomUUID()}"
        enc.status = Encounter.EncounterStatus.FINISHED
        if (!patientRef.reference.isNullOrBlank()) enc.subject = patientRef

        val period = Period()
        parseToDate(s.inicio)?.let { period.start = it }
        parseToDate(s.fin)?.let { period.end = it }
        enc.period = period

        enc.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val resumen = "Sesión de chat. Inicio: ${s.inicio ?: "N/D"}, Fin: ${s.fin ?: "N/D"}."
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div").apply { addText(resumen) }
        }
        return enc
    }

    private fun buildCommunication(
        s: SesionChatEntity,
        patientRef: Reference,
        encounter: Encounter
    ): Communication {
        val cm = Communication()
        cm.id = "Communication/${UUID.randomUUID()}"
        cm.status = Communication.CommunicationStatus.COMPLETED

        if (!patientRef.reference.isNullOrBlank()) cm.subject = patientRef
        cm.partOf = listOf(Reference("urn:uuid:${encounter.idElement.idPart}"))

        parseToDate(s.inicio)?.let { cm.sent = it }
        parseToDate(s.fin)?.let { cm.received = it }

        s.contenido?.takeIf { it.isNotBlank() }?.let { texto ->
            cm.payload = listOf(
                Communication.CommunicationPayloadComponent().apply {
                    content = StringType(texto)
                }
            )
        }

        cm.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val resumen = buildString {
                append("Comunicación de chat.")
                s.contenido?.takeIf { it.isNotBlank() }?.let { append(" Contenido: ${it.take(140)}") }
            }
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div").apply { addText(resumen) }
        }

        return cm
    }

    private fun parseToDate(value: Any?): Date? = when (value) {
        null -> null
        is java.util.Date -> value
        is java.time.LocalDateTime -> Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
        is java.time.LocalDate -> Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant())
        is String -> try {
            // intenta ISO_LOCAL_DATE_TIME y luego ISO_LOCAL_DATE
            java.time.LocalDateTime.parse(value).let { Date.from(it.atZone(ZoneId.systemDefault()).toInstant()) }
        } catch (_: Exception) {
            try {
                java.time.LocalDate.parse(value).let { Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant()) }
            } catch (_: Exception) { null }
        }
        else -> null
    }
}
