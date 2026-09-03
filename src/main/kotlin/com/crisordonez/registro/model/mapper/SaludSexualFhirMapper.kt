package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.SaludSexualEntity
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.utilities.xhtml.XhtmlNode
import java.time.ZoneId
import java.util.Date
import java.util.UUID

object SaludSexualFhirMapper {

    fun toFhirBundle(s: SaludSexualEntity): Bundle {
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            id = "Bundle/${UUID.randomUUID()}"
        }

        val patientRef = buildPatientReference(s)

        val qr = buildQuestionnaireResponse(s, patientRef)
        bundle.addEntry(Bundle.BundleEntryComponent().apply {
            fullUrl = "urn:uuid:${qr.idElement.idPart}"
            resource = qr
        })

        buildPregnancyObservation(s, patientRef)?.let { obs ->
            bundle.addEntry(Bundle.BundleEntryComponent().apply {
                fullUrl = "urn:uuid:${obs.idElement.idPart}"
                resource = obs
            })
        }

        buildLmpObservation(s, patientRef)?.let { obs ->
            bundle.addEntry(Bundle.BundleEntryComponent().apply {
                fullUrl = "urn:uuid:${obs.idElement.idPart}"
                resource = obs
            })
        }

        return bundle
    }

    private fun buildPatientReference(s: SaludSexualEntity): Reference {
        val pid = try { s.paciente?.publicId?.toString() } catch (_: Exception) { null }
        return if (!pid.isNullOrBlank()) Reference("Patient/$pid")
        else Reference().apply { display = "Paciente no resuelto" }
    }

    private fun buildQuestionnaireResponse(
        s: SaludSexualEntity,
        patientRef: Reference
    ): QuestionnaireResponse {
        val qr = QuestionnaireResponse().apply {
            id = "QuestionnaireResponse/${UUID.randomUUID()}"
            status = QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED
            if (!patientRef.reference.isNullOrBlank()) subject = patientRef
        }

        val items = mutableListOf<QuestionnaireResponse.QuestionnaireResponseItemComponent>()

        // ¿Está embarazada?
        s.estaEmbarazada?.let { value ->
            items += item("esta-embarazada", "¿Está embarazada?", booleanAnswer(value))
        }

        // Fecha de última menstruación (acepta String, Date o LocalDate)
        s.fechaUltimaMenstruacion?.let { anyDate ->
            dateAnswer(anyDate)?.let { ans ->
                items += item("fecha-ultima-menstruacion", "Fecha de última menstruación", ans)
            }
        }

        // Último examen de Papanicolaou (código del catálogo rango_tiempo_examen)
        s.ultimoExamenPap.takeIf { it.isNotBlank() }?.let { code ->
            items += item("ultimo-examen-pap", "Último examen Papanicolaou", stringAnswer(code))
        }

        // Tiempo desde última prueba VPH (código del catálogo rango_tiempo_examen)
        s.tiempoPruebaVph.takeIf { it.isNotBlank() }?.let { code ->
            items += item("tiempo-prueba-vph", "Tiempo desde última prueba VPH", stringAnswer(code))
        }

        // Número de parejas sexuales
        s.numParejasSexuales?.let { n ->
            items += item("num-parejas-sexuales", "Número de parejas sexuales", integerAnswer(n))
        }

        // ¿Tiene ETS? (enum -> string)
        s.tieneEts?.let { e ->
            items += item("tiene-ets", "¿Tiene ETS?", stringAnswer(e.name))
        }

        // Nombre de ETS (texto)
        s.nombreEts?.takeIf { it.isNotBlank() }?.let { txt ->
            items += item("nombre-ets", "Nombre de ETS", stringAnswer(txt))
        }

        qr.item = items
        qr.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val resumen = buildString {
                append("Cuestionario Salud Sexual. ")
                s.estaEmbarazada?.let { append("Embarazo: ${if (it) "Sí" else "No"}. ") }
                s.fechaUltimaMenstruacion?.let { append("FUM: $it. ") }
                s.ultimoExamenPap.takeIf { it.isNotBlank() }?.let { append("Pap: $it. ") }
                s.tiempoPruebaVph.takeIf { it.isNotBlank() }?.let { append("Tiempo prueba VPH: $it. ") }
                s.numParejasSexuales?.let { append("Parejas: $it. ") }
                s.tieneEts?.let { append("Tiene ETS: ${it.name}. ") }
                s.nombreEts?.takeIf { it.isNotBlank() }?.let { append("ETS: $it.") }
            }
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div").apply { addText(resumen) }
        }

        return qr
    }

    private fun buildPregnancyObservation(
        s: SaludSexualEntity,
        patientRef: Reference
    ): Observation? {
        val value = s.estaEmbarazada ?: return null

        val obs = Observation()
        obs.id = "Observation/${UUID.randomUUID()}"
        obs.status = Observation.ObservationStatus.FINAL
        obs.category = listOf(
            CodeableConcept().addCoding(
                Coding("http://terminology.hl7.org/CodeSystem/observation-category", "social-history", "Social History")
            )
        )
        obs.code = CodeableConcept().apply { text = "Pregnancy status" }
        obs.value = CodeableConcept().apply { text = if (value) "Positive" else "Negative" }

        if (!patientRef.reference.isNullOrBlank()) obs.subject = patientRef
        return obs
    }

    private fun buildLmpObservation(
        s: SaludSexualEntity,
        patientRef: Reference
    ): Observation? {
        val date: Date = parseToDate(s.fechaUltimaMenstruacion) ?: return null

        val obs = Observation()
        obs.id = "Observation/${UUID.randomUUID()}"
        obs.status = Observation.ObservationStatus.FINAL
        obs.category = listOf(
            CodeableConcept().addCoding(
                Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")
            )
        )
        obs.code = CodeableConcept().apply { text = "Last menstrual period date" }
        obs.effective = DateTimeType(date)
        obs.value = DateTimeType(date)
        if (!patientRef.reference.isNullOrBlank()) obs.subject = patientRef

        return obs
    }

    private fun item(
        linkId: String,
        text: String,
        answer: QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent
    ): QuestionnaireResponse.QuestionnaireResponseItemComponent {
        return QuestionnaireResponse.QuestionnaireResponseItemComponent().apply {
            this.linkId = linkId
            this.text = text
            this.answer = listOf(answer)
        }
    }

    private fun booleanAnswer(value: Boolean) =
        QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent().apply {
            this.value = BooleanType(value)
        }

    private fun stringAnswer(value: String) =
        QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent().apply {
            this.value = StringType(value)
        }

    private fun integerAnswer(value: Int) =
        QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent().apply {
            this.value = IntegerType(value)
        }

    private fun dateAnswer(value: Any): QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent? {
        val date = parseToDate(value) ?: return null
        return QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent().apply {
            this.value = DateType(date)
        }
    }

    private fun parseToDate(value: Any?): Date? {
        return when (value) {
            null -> null
            is java.util.Date -> value
            is java.time.LocalDate -> Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant())
            is String -> try {
                // espera yyyy-MM-dd
                java.sql.Date.valueOf(value)
            } catch (_: Exception) {
                null
            }
            else -> null
        }
    }
}
